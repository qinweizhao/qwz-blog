package com.qinweizhao.blog.framework.listener.comment;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.StrBuilder;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.event.comment.CommentNewEvent;
import com.qinweizhao.blog.framework.event.comment.CommentReplyEvent;
import com.qinweizhao.blog.mail.MailService;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.properties.CommentProperties;
import com.qinweizhao.blog.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PostComment event listener.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-04-23
 */
@Slf4j
@Component
@AllArgsConstructor
public class CommentEventListener {

    private final MailService mailService;

    private final ConfigService configService;

    private final CommentService commentService;

    private final PostService postService;

    private final JournalService journalService;

    private final UserService userService;

    private final ThemeService themeService;


    /**
     * 收到一个新的新评论事件
     *
     * @param newEvent newEvent
     */
    @Async
    @EventListener
    public void handleCommentNewEvent(CommentNewEvent newEvent) {
        Boolean newCommentNotice = configService.getByPropertyOrDefault(CommentProperties.NEW_NOTICE, Boolean.class, false);

        if (Boolean.FALSE.equals(newCommentNotice)) {
            return;
        }

        User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));

        Map<String, Object> data = new LinkedHashMap<>();

        StringBuilder subject = new StringBuilder();


        CommentDTO commentDTO = commentService.getById(newEvent.getCommentId());

        CommentType type = commentDTO.getType();
        Integer targetId = commentDTO.getTargetId();

        if (CommentType.POST.equals(type)) {

            log.debug("收到文章评论: [{}]", commentDTO);

            PostSimpleDTO postSimpleDTO = postService.getSimpleById(targetId);
            data.put("pageFullPath", postSimpleDTO.getFullPath());
            data.put("pageTitle", postSimpleDTO.getTitle());
            data.put("author", commentDTO.getAuthor());
            data.put("content", commentDTO.getContent());

            subject.append("您的博客文章《").append(postSimpleDTO.getTitle()).append("》有了新的评论。");

        } else if (CommentType.JOURNAL.equals(type)) {

            log.debug("收到日志评论: [{}]", commentDTO);

            JournalDTO journalDTO = journalService.getById(targetId);

            StrBuilder url = new StrBuilder(configService.getBlogBaseUrl()).append("/").append(configService.getJournalsPrefix());
            data.put("pageFullPath", url.toString());
            data.put("pageTitle", journalDTO.getCreateTime());
            data.put("author", commentDTO.getAuthor());
            data.put("content", commentDTO.getContent());

            subject.append("您的博客日志有了新的评论");
        }

        String template = "common/mail_template/mail_notice.ftl";

        if (themeService.templateExists("mail_template/mail_notice.ftl")) {
            template = themeService.renderWithSuffix("mail_template/mail_notice");
        }

        mailService.sendTemplateMail(user.getEmail(), subject.toString(), data, template);
    }

    /**
     * 收到新的回复评论事件
     *
     * @param replyEvent reply comment event.
     */
    @Async
    @EventListener
    public void handleCommentReplyEvent(CommentReplyEvent replyEvent) {
        Boolean replyCommentNotice = configService.getByPropertyOrDefault(CommentProperties.REPLY_NOTICE, Boolean.class, false);
        if (!replyCommentNotice) {
            return;
        }
        String baseAuthorEmail = "";
        String blogTitle = configService.getBlogTitle();

        Map<String, Object> data = new LinkedHashMap<>();
        StringBuilder subject = new StringBuilder();


        CommentDTO commentDTO = commentService.getById(replyEvent.getCommentId());
        CommentType type = commentDTO.getType();

        if (CommentType.POST.equals(type)) {

            CommentDTO baseComment = commentService.getById(commentDTO.getParentId());

            if (StringUtils.isEmpty(baseComment.getEmail()) && !Validator.isEmail(baseComment.getEmail())) {
                return;
            }
            if (!baseComment.getAllowNotification()) {
                return;
            }

            baseAuthorEmail = baseComment.getEmail();
            PostSimpleDTO postSimpleDTO = postService.getSimpleById(commentDTO.getTargetId());

            data.put("pageFullPath",  postSimpleDTO.getFullPath());
            data.put("pageTitle", postSimpleDTO.getTitle());
            data.put("baseAuthor", baseComment.getAuthor());
            data.put("baseContent", baseComment.getContent());
            data.put("replyAuthor", commentDTO.getAuthor());
            data.put("replyContent", commentDTO.getContent());

            subject.append("您在【").append(blogTitle).append("】评论的文章《").append(postSimpleDTO.getTitle()).append("》有了新的评论。");
        } else if (CommentType.JOURNAL.equals(type)) {

            CommentDTO baseComment = commentService.getById(commentDTO.getParentId());

            if (StringUtils.isEmpty(baseComment.getEmail()) && !Validator.isEmail(baseComment.getEmail())) {
                return;
            }
            if (Boolean.FALSE.equals(baseComment.getAllowNotification())) {
                return;
            }

            baseAuthorEmail = baseComment.getEmail();

            JournalDTO journal = journalService.getById(commentDTO.getTargetId());

            StrBuilder url = new StrBuilder(configService.getBlogBaseUrl()).append("/").append(configService.getJournalsPrefix());
            data.put("pageFullPath", url);
            data.put("pageTitle", journal.getContent());
            data.put("baseAuthor", baseComment.getAuthor());
            data.put("baseContent", baseComment.getContent());
            data.put("replyAuthor", commentDTO.getAuthor());
            data.put("replyContent", commentDTO.getContent());

            subject.append("您在【").append(blogTitle).append("】评论的日志").append("有了新的评论。");
        }

        String template = "common/mail_template/mail_reply.ftl";
        if (themeService.templateExists("mail_template/mail_reply.ftl")) {
            template = themeService.renderWithSuffix("mail_template/mail_reply");
        }

        mailService.sendTemplateMail(baseAuthorEmail, subject.toString(), data, template);
    }
}
