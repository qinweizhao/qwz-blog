//package com.qinweizhao.blog.service.impl;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.qinweizhao.blog.model.base.BaseEntity;
//import com.qinweizhao.blog.model.entity.Comment;
//import org.springframework.stereotype.Service;
//import com.qinweizhao.blog.service.*;
//
///**
// * DataProcessService implementation.
// *
// * @author ryanwang
// * @since 2019-12-29
// */
//@Service
//public class DataProcessServiceImpl implements DataProcessService {
//
//    private final PostService postService;
//
//    private final SheetService sheetService;
//
//    private final CommentService<BaseMapper<Comment>, BaseEntity> commentService;
//
//    private final SheetCommentService sheetCommentService;
//
//    private final JournalCommentService journalCommentService;
//
//    private final AttachmentService attachmentService;
//
//    private final OptionService optionService;
//
//    private final PhotoService photoService;
//
//    private final ThemeSettingService themeSettingService;
//
//    public DataProcessServiceImpl(PostService postService,
//            SheetService sheetService,
//            CommentService<BaseMapper<Comment>, BaseEntity> commentService,
//            SheetCommentService sheetCommentService,
//            JournalCommentService journalCommentService,
//            AttachmentService attachmentService,
//            OptionService optionService,
//            PhotoService photoService,
//            ThemeSettingService themeSettingService) {
//        this.postService = postService;
//        this.sheetService = sheetService;
//        this.commentService = commentService;
//        this.sheetCommentService = sheetCommentService;
//        this.journalCommentService = journalCommentService;
//        this.attachmentService = attachmentService;
//        this.optionService = optionService;
//        this.photoService = photoService;
//        this.themeSettingService = themeSettingService;
//    }
//
//    @Override
//    public void replaceAllUrl(String oldUrl, String newUrl) {
//        postService.replaceUrl(oldUrl, newUrl);
//        sheetService.replaceUrl(oldUrl, newUrl);
//        commentService.replaceUrl(oldUrl, newUrl);
//        sheetCommentService.replaceUrl(oldUrl, newUrl);
//        journalCommentService.replaceUrl(oldUrl, newUrl);
//        attachmentService.replaceUrl(oldUrl, newUrl);
//        optionService.replaceUrl(oldUrl, newUrl);
//        photoService.replaceUrl(oldUrl, newUrl);
//        themeSettingService.replaceUrl(oldUrl, newUrl);
//    }
//}
