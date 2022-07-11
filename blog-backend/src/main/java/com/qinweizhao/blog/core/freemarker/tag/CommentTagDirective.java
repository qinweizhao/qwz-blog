//package com.qinweizhao.blog.core.freemarker.tag;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.qinweizhao.blog.model.base.BaseEntity;
//import com.qinweizhao.blog.model.entity.Comment;
//import com.qinweizhao.blog.model.enums.CommentStatus;
//import com.qinweizhao.blog.model.support.HaloConst;
//import com.qinweizhao.blog.service.CommentService;
//import freemarker.core.Environment;
//import freemarker.template.*;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Map;
//
///**
// * Freemarker custom tag of comment.
// *
// * @author ryanwang
// * @date 2019-03-22
// */
//@Component
//public class CommentTagDirective implements TemplateDirectiveModel {
//
//    private final CommentService<BaseMapper<Comment>, BaseEntity> commentService;
//
//    public CommentTagDirective(Configuration configuration, CommentService<BaseMapper<Comment>, BaseEntity> commentService) {
//        this.commentService = commentService;
//        configuration.setSharedVariable("commentTag", this);
//    }
//
//    @Override
//    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
//        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
//
//        if (params.containsKey(HaloConst.METHOD_KEY)) {
//            String method = params.get(HaloConst.METHOD_KEY).toString();
//            switch (method) {
//                case "latest":
//                    int top = Integer.parseInt(params.get("top").toString());
//                    Page<PostComment> postComments = commentService.pageLatest(top, CommentStatus.PUBLISHED);
//                    env.setVariable("comments", builder.build().wrap(commentService.convertToWithPostVo(postComments)));
//                    break;
//                case "count":
//                    env.setVariable("count", builder.build().wrap(commentService.count()));
//                    break;
//                default:
//                    break;
//            }
//        }
//        body.render(env.getOut());
//    }
//}
