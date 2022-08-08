package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.mail.MailService;
import com.qinweizhao.blog.model.param.MailParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Mail controller.
 *
 * @author johnniang
 * @author qinweizhao
 * @date 2019-05-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/mails")
public class MailController {

    private final MailService mailService;

    @DisableOnCondition
    @PostMapping("test")
    public String testMail(@Valid @RequestBody MailParam mailParam) {
        mailService.sendTextMail(mailParam.getTo(), mailParam.getSubject(), mailParam.getContent());
        return "已发送，请查收。若确认没有收到邮件，请检查服务器日志";
    }

    @DisableOnCondition
    @PostMapping("test/connection")
    public String testConnection() {
        mailService.testConnection();
        return "您和邮箱服务器的连接通畅";
    }

}
