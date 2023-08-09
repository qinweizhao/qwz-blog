package com.qinweizhao.blog.mail;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Properties;

/**
 * Java mail sender factory.
 *
 * @author qinweizhao
 */
public class MailSenderFactory {

    /**
     * Get mail sender.
     *
     * @param mailProperties mail properties must not be null
     * @return java mail sender
     */

    public JavaMailSender getMailSender(MailProperties mailProperties) {
        Assert.notNull(mailProperties, "邮件属性不能为空");

        // 创建邮件发件人
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // 设置属性
        setProperties(mailSender, mailProperties);

        return mailSender;
    }

    private void setProperties(JavaMailSenderImpl mailSender, MailProperties mailProperties) {
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setProtocol(mailProperties.getProtocol());
        mailSender.setDefaultEncoding(mailProperties.getDefaultEncoding().name());

        if (!CollectionUtils.isEmpty(mailProperties.getProperties())) {
            Properties properties = new Properties();
            properties.putAll(mailProperties.getProperties());
            mailSender.setJavaMailProperties(properties);
        }
    }
}
