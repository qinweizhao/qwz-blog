package com.qinweizhao.blog.mail;

import com.qinweizhao.blog.framework.event.config.ConfigUpdatedEvent;
import com.qinweizhao.blog.service.ConfigService;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;

/**
 * Mail service implementation.
 * todo 配置变更通知？
 * @author qinweizhao
 * @since 2019-03-17
 */
@Slf4j
@Service
public class MailServiceImpl extends AbstractMailService implements ApplicationListener<ConfigUpdatedEvent> {

    private final FreeMarkerConfigurer freeMarker;

    public MailServiceImpl(FreeMarkerConfigurer freeMarker, ConfigService configService) {
        super(configService);
        this.freeMarker = freeMarker;
    }

    @Override
    public boolean sendTextMail(String to, String subject, String content) {
        sendMailTemplate(false, messageHelper -> {
            messageHelper.setSubject(subject);
            messageHelper.setTo(to);
            messageHelper.setText(content);
        });
        return true;
    }

    @Override
    public void sendTemplateMail(String to, String subject, Map<String, Object> content, String templateName) {
        sendMailTemplate(true, messageHelper -> {
            // 构建消息内容
            Template template = freeMarker.getConfiguration().getTemplate(templateName);
            String contentResult = FreeMarkerTemplateUtils.processTemplateIntoString(template, content);

            messageHelper.setSubject(subject);
            messageHelper.setTo(to);
            messageHelper.setText(contentResult, true);
        });
    }

    @Override
    public void onApplicationEvent(@NotNull ConfigUpdatedEvent event) {
        clearCache();
    }
}
