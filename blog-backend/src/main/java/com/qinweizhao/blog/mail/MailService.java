package com.qinweizhao.blog.mail;

import java.util.Map;

/**
 * Mail service interface.
 *
 * @author ryanwang
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
    void sendTextMail(String to, String subject, String content);

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
     * 发送带有附件的邮件
     *
     * @param to             recipient
     * @param subject        subject
     * @param content        content
     * @param templateName   template name
     * @param attachFilePath attachment full path name
     */
    void sendAttachMail(String to, String subject, Map<String, Object> content, String templateName, String attachFilePath);

    /**
     * 测试电子邮件服务器连接
     */
    void testConnection();
}
