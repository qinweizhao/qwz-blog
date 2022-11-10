package com.qinweizhao.blog.mail;

import java.util.Map;

/**
 * Mail service interface.
 *
 * @since 2019-03-17
 */
public interface MailService {

    /**
     * 发送一封简单的电子邮件
     *
     * @param to      recipient
     * @param subject subject
     * @param content content
     */
    boolean sendTextMail(String to, String subject, String content);

    /**
     * 使用 html 发送电子邮件
     *
     * @param to           recipient
     * @param subject      subject
     * @param content      content
     * @param templateName template name
     */
    void sendTemplateMail(String to, String subject, Map<String, Object> content, String templateName);

    /**
     * 测试电子邮件服务器连接
     */
    void testConnection();
}
