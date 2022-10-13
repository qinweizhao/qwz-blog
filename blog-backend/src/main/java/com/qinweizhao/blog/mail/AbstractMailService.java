package com.qinweizhao.blog.mail;

import com.qinweizhao.blog.exception.EmailException;
import com.qinweizhao.blog.model.properties.EmailProperties;
import com.qinweizhao.blog.service.ConfigService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract mail service.
 *
 * @author johnniang
 */
@Slf4j
public abstract class AbstractMailService implements MailService {

    private static final int DEFAULT_POOL_SIZE = 5;
    protected final ConfigService configService;
    private JavaMailSender cachedMailSender;
    private MailProperties cachedMailProperties;
    private String cachedFromName;
    @Nullable
    private ExecutorService executorService;

    protected AbstractMailService(ConfigService configService) {
        this.configService = configService;
    }

    @NonNull
    public ExecutorService getExecutorService() {
        if (this.executorService == null) {
            this.executorService = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
        }
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Test connection with email server.
     */
    @Override
    public void testConnection() {
        JavaMailSender javaMailSender = getMailSender();
        if (javaMailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
            try {
                mailSender.testConnection();
            } catch (MessagingException e) {
                throw new EmailException("无法连接到邮箱服务器，请检查邮箱配置.[" + e.getMessage() + "]", e);
            }
        }
    }

    /**
     * 发送邮件模板
     *
     * @param callback mime message callback.
     */
    protected void sendMailTemplate(@Nullable Callback callback) {
        if (callback == null) {
            log.info("回调为空，跳过发送邮件");
            return;
        }

        // 检查邮件是否启用
        Boolean emailEnabled = configService.getByPropertyOrDefault(EmailProperties.ENABLED, Boolean.class);

        if (Boolean.FALSE.equals(emailEnabled)) {
            log.info("电子邮件已被禁用，可以通过管理页面上的电子邮件设置重新启用它");
            return;
        }

        // 获取邮件发件人
        JavaMailSender mailSender = getMailSender();
        printMailConfig();

        MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage());

        try {
            messageHelper.setFrom(getFromAddress(mailSender));

            callback.handle(messageHelper);

            MimeMessage mimeMessage = messageHelper.getMimeMessage();

            mailSender.send(mimeMessage);

            log.info("发送电子邮件至 [{}] 成功， 标题: [{}]， 内容: [{}]", Arrays.toString(mimeMessage.getAllRecipients()), mimeMessage.getSubject(), mimeMessage.getSentDate());
        } catch (Exception e) {
            throw new EmailException("邮件发送失败，请检查 SMTP 服务配置是否正确", e);
        }
    }

    /**
     * Send mail template if executor service is enable.
     *
     * @param callback   callback message handler
     * @param tryToAsync if the send procedure should try to asynchronous
     */
    protected void sendMailTemplate(boolean tryToAsync, @Nullable Callback callback) {
        ExecutorService executorService = getExecutorService();
        if (tryToAsync) {
            // 异步发送邮件
            executorService.execute(() -> sendMailTemplate(callback));
        } else {
            // 同步发送邮件
            sendMailTemplate(callback);
        }
    }

    /**
     * Get java mail sender.
     *
     * @return java mail sender
     */
    @NonNull
    private synchronized JavaMailSender getMailSender() {
        if (this.cachedMailSender == null) {
            // 创建邮件发件人工厂
            MailSenderFactory mailSenderFactory = new MailSenderFactory();
            // 获取邮件发件人
            this.cachedMailSender = mailSenderFactory.getMailSender(getMailProperties());
        }

        return this.cachedMailSender;
    }

    /**
     * Get from-address.
     *
     * @param javaMailSender java mail sender.
     * @return from-name internet address
     * @throws UnsupportedEncodingException throws when you give a wrong character encoding
     */
    private synchronized InternetAddress getFromAddress(@NonNull JavaMailSender javaMailSender) throws UnsupportedEncodingException {
        Assert.notNull(javaMailSender, "Java mail sender must not be null");

        if (StringUtils.isBlank(this.cachedFromName)) {
            // set personal name
            this.cachedFromName = configService.getByPropertyOfNonNull(EmailProperties.FROM_NAME).toString();
        }

        if (javaMailSender instanceof JavaMailSenderImpl) {
            // get user name(email)
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
            String username = mailSender.getUsername();

            // build internet address
            return new InternetAddress(username, this.cachedFromName, mailSender.getDefaultEncoding());
        }

        throw new UnsupportedOperationException("Unsupported java mail sender: " + javaMailSender.getClass().getName());
    }

    /**
     * Get mail properties.
     *
     * @return mail properties
     */
    @NonNull
    private synchronized MailProperties getMailProperties() {
        if (cachedMailProperties == null) {
            // 创建邮件属性
            MailProperties mailProperties = new MailProperties(log.isDebugEnabled());

            // 设置属性
            mailProperties.setHost(configService.getByPropertyOrDefault(EmailProperties.HOST, String.class));
            mailProperties.setPort(configService.getByPropertyOrDefault(EmailProperties.SSL_PORT, Integer.class));
            mailProperties.setUsername(configService.getByPropertyOrDefault(EmailProperties.USERNAME, String.class));
            mailProperties.setPassword(configService.getByPropertyOrDefault(EmailProperties.PASSWORD, String.class));
            mailProperties.setProtocol(configService.getByPropertyOrDefault(EmailProperties.PROTOCOL, String.class));
            this.cachedMailProperties = mailProperties;
        }

        return this.cachedMailProperties;
    }

    /**
     * Print mail configuration.
     */
    private void printMailConfig() {
        if (!log.isDebugEnabled()) {
            return;
        }

        // get mail properties
        MailProperties mailProperties = getMailProperties();
        log.debug(mailProperties.toString());
    }

    /**
     * Clear cached instance.
     */
    protected void clearCache() {
        this.cachedMailSender = null;
        this.cachedFromName = null;
        this.cachedMailProperties = null;
        log.debug("Cleared all mail caches");
    }

    /**
     * Message callback.
     */
    protected interface Callback {
        /**
         * Handle message set.
         *
         * @param messageHelper mime message helper
         */
        void handle(@NonNull MimeMessageHelper messageHelper) throws MessagingException, IOException, TemplateException;
    }
}
