//package com.qinweizhao.blog.listener.comment;
//
//import cn.hutool.core.lang.Validator;
//import cn.hutool.core.text.StrBuilder;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.qinweizhao.blog.event.comment.CommentNewEvent;
//import com.qinweizhao.blog.event.comment.CommentReplyEvent;
//import com.qinweizhao.blog.exception.ServiceException;
//import com.qinweizhao.blog.mail.MailService;
//import com.qinweizhao.blog.model.base.BaseEntity;
//import com.qinweizhao.blog.model.dto.post.BasePostMinimalDTO;
//import com.qinweizhao.blog.model.entity.Comment;
//import com.qinweizhao.blog.model.entity.Journal;
//import com.qinweizhao.blog.model.entity.User;
//import com.qinweizhao.blog.model.properties.CommentProperties;
//import com.qinweizhao.blog.service.*;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * PostComment event listener.
// *
// * @author johnniang
// * @author ryanwang
// * @date 2019-04-23
// */
//@Slf4j
//@Component
//public class CommentEventListener {
//
//    private final MailService mailService;
//
//    private final OptionService optionService;
//
//    private final CommentService<BaseMapper<Comment>, BaseEntity> commentService;
//
//    private final SheetCommentService sheetCommentService;
//
//    private final JournalCommentService journalCommentService;
//
//    private final PostService postService;
//
//    private final SheetService sheetService;
//
//    private final JournalService journalService;
//
//    private final UserService userService;
//
//    private final ThemeService themeService;
//
//    public CommentEventListener(MailService mailService, OptionService optionService, CommentService<BaseMapper<Comment>, BaseEntity> commentService, SheetCommentService sheetCommentService, JournalCommentService journalCommentService, PostService postService, SheetService sheetService, JournalService journalService, UserService userService, ThemeService themeService) {
//        this.mailService = mailService;
//        this.optionService = optionService;
//        this.commentService = commentService;
//        this.sheetCommentService = sheetCommentService;
//        this.journalCommentService = journalCommentService;
//        this.postService = postService;
//        this.sheetService = sheetService;
//        this.journalService = journalService;
//        this.userService = userService;
//        this.themeService = themeService;
//    }
//
//    /**
//     * Received a new new comment event.
//     *
//     * @param newEvent new comment event.
//     */
//    @Async
//    @EventListener
//    public void handleCommentNewEvent(CommentNewEvent newEvent) {
//        Boolean newCommentNotice = optionService.getByPropertyOrDefault(CommentProperties.NEW_NOTICE, Boolean.class, false);
//
//        if (!newCommentNotice) {
//            // Skip mailing
//            return;
//        }
//
//        User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("????????????????????????"));
//
//        Map<String, Object> data = new HashMap<>();
//
//        StringBuilder subject = new StringBuilder();
//
//        Boolean enabledAbsolutePath = optionService.isEnabledAbsolutePath();
//
//        if (newEvent.getSource() instanceof CommentService) {
//            // Get postComment id
//            PostComment postComment = commentService.getById(newEvent.getCommentId());
//
//            log.debug("Got post comment: [{}]", postComment);
//
//            BasePostMinimalDTO post = postService.convertToMinimal(postService.getById(postComment.getPostId()));
//
//            data.put("pageFullPath", enabledAbsolutePath ? post.getFullPath() : optionService.getBlogBaseUrl() + post.getFullPath());
//            data.put("pageTitle", post.getTitle());
//            data.put("author", postComment.getAuthor());
//            data.put("content", postComment.getContent());
//
//            subject.append("?????????????????????")
//                    .append(post.getTitle())
//                    .append("????????????????????????");
//
//        } else if (newEvent.getSource() instanceof SheetCommentService) {
//            SheetComment sheetComment = sheetCommentService.getById(newEvent.getCommentId());
//
//            log.debug("Got sheet comment: [{}]", sheetComment);
//
//            BasePostMinimalDTO sheet = sheetService.convertToMinimal(sheetService.getById(sheetComment.getPostId()));
//
//            data.put("pageFullPath", enabledAbsolutePath ? sheet.getFullPath() : optionService.getBlogBaseUrl() + sheet.getFullPath());
//            data.put("pageTitle", sheet.getTitle());
//            data.put("author", sheetComment.getAuthor());
//            data.put("content", sheetComment.getContent());
//
//            subject.append("?????????????????????")
//                    .append(sheet.getTitle())
//                    .append("????????????????????????");
//        } else if (newEvent.getSource() instanceof JournalCommentService) {
//            JournalComment journalComment = journalCommentService.getById(newEvent.getCommentId());
//
//            log.debug("Got journal comment: [{}]", journalComment);
//
//            Journal journal = journalService.getById(journalComment.getPostId());
//
//            StrBuilder url = new StrBuilder(optionService.getBlogBaseUrl())
//                    .append("/")
//                    .append(optionService.getJournalsPrefix());
//            data.put("pageFullPath", url.toString());
//            data.put("pageTitle", journal.getCreateTime());
//            data.put("author", journalComment.getAuthor());
//            data.put("content", journalComment.getContent());
//
//            subject.append("????????????????????????????????????");
//        }
//
//        String template = "common/mail_template/mail_notice.ftl";
//
//        if (themeService.templateExists("mail_template/mail_notice.ftl")) {
//            template = themeService.renderWithSuffix("mail_template/mail_notice");
//        }
//
//        mailService.sendTemplateMail(user.getEmail(), subject.toString(), data, template);
//    }
//
//    /**
//     * Received a new reply comment event.
//     *
//     * @param replyEvent reply comment event.
//     */
//    @Async
//    @EventListener
//    public void handleCommentReplyEvent(CommentReplyEvent replyEvent) {
//        Boolean replyCommentNotice = optionService.getByPropertyOrDefault(CommentProperties.REPLY_NOTICE, Boolean.class, false);
//
//        if (!replyCommentNotice) {
//            // Skip mailing
//            return;
//        }
//
//        String baseAuthorEmail = "";
//
//        String blogTitle = optionService.getBlogTitle();
//
//        Map<String, Object> data = new HashMap<>();
//
//        StringBuilder subject = new StringBuilder();
//
//        Boolean enabledAbsolutePath = optionService.isEnabledAbsolutePath();
//
//        log.debug("replyEvent.getSource():" + replyEvent.getSource().toString());
//
//        if (replyEvent.getSource() instanceof CommentService) {
//
//            PostComment postComment = commentService.getById(replyEvent.getCommentId());
//
//            PostComment baseComment = commentService.getById(postComment.getParentId());
//
//            if (StringUtils.isEmpty(baseComment.getEmail()) && !Validator.isEmail(baseComment.getEmail())) {
//                return;
//            }
//
//            if (!baseComment.getAllowNotification()) {
//                return;
//            }
//
//            baseAuthorEmail = baseComment.getEmail();
//
//            BasePostMinimalDTO post = postService.convertToMinimal(postService.getById(postComment.getPostId()));
//
//            data.put("pageFullPath", enabledAbsolutePath ? post.getFullPath() : optionService.getBlogBaseUrl() + post.getFullPath());
//            data.put("pageTitle", post.getTitle());
//            data.put("baseAuthor", baseComment.getAuthor());
//            data.put("baseContent", baseComment.getContent());
//            data.put("replyAuthor", postComment.getAuthor());
//            data.put("replyContent", postComment.getContent());
//
//            subject.append("?????????")
//                    .append(blogTitle)
//                    .append("?????????????????????")
//                    .append(post.getTitle())
//                    .append("????????????????????????");
//        } else if (replyEvent.getSource() instanceof SheetCommentService) {
//
//            SheetComment sheetComment = sheetCommentService.getById(replyEvent.getCommentId());
//
//            SheetComment baseComment = sheetCommentService.getById(sheetComment.getParentId());
//
//            if (StringUtils.isEmpty(baseComment.getEmail()) && !Validator.isEmail(baseComment.getEmail())) {
//                return;
//            }
//
//            if (!baseComment.getAllowNotification()) {
//                return;
//            }
//
//            baseAuthorEmail = baseComment.getEmail();
//
//            BasePostMinimalDTO sheet = sheetService.convertToMinimal(sheetService.getById(sheetComment.getPostId()));
//
//            data.put("pageFullPath", enabledAbsolutePath ? sheet.getFullPath() : optionService.getBlogBaseUrl() + sheet.getFullPath());
//            data.put("pageTitle", sheet.getTitle());
//            data.put("baseAuthor", baseComment.getAuthor());
//            data.put("baseContent", baseComment.getContent());
//            data.put("replyAuthor", sheetComment.getAuthor());
//            data.put("replyContent", sheetComment.getContent());
//
//            subject.append("?????????")
//                    .append(blogTitle)
//                    .append("?????????????????????")
//                    .append(sheet.getTitle())
//                    .append("????????????????????????");
//        } else if (replyEvent.getSource() instanceof JournalCommentService) {
//            JournalComment journalComment = journalCommentService.getById(replyEvent.getCommentId());
//
//            JournalComment baseComment = journalCommentService.getById(journalComment.getParentId());
//
//            if (StringUtils.isEmpty(baseComment.getEmail()) && !Validator.isEmail(baseComment.getEmail())) {
//                return;
//            }
//
//            if (!baseComment.getAllowNotification()) {
//                return;
//            }
//
//            baseAuthorEmail = baseComment.getEmail();
//
//            Journal journal = journalService.getById(journalComment.getPostId());
//
//            StrBuilder url = new StrBuilder(optionService.getBlogBaseUrl())
//                    .append("/")
//                    .append(optionService.getJournalsPrefix());
//            data.put("pageFullPath", url);
//            data.put("pageTitle", journal.getContent());
//            data.put("baseAuthor", baseComment.getAuthor());
//            data.put("baseContent", baseComment.getContent());
//            data.put("replyAuthor", journalComment.getAuthor());
//            data.put("replyContent", journalComment.getContent());
//
//            subject.append("?????????")
//                    .append(blogTitle)
//                    .append("??????????????????")
//                    .append("?????????????????????");
//        }
//
//        String template = "common/mail_template/mail_reply.ftl";
//
//        if (themeService.templateExists("mail_template/mail_reply.ftl")) {
//            template = themeService.renderWithSuffix("mail_template/mail_reply");
//        }
//
//        mailService.sendTemplateMail(baseAuthorEmail, subject.toString(), data, template);
//    }
//}
