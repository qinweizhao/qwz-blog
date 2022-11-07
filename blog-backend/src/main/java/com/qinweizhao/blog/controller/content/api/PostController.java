package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.framework.cache.lock.CacheLock;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.CommentParam;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;

/**
 * 前台-文章
 *
 * @author johnniang
 * @author qinweizhao
 * @since 2019-04-02
 */
@RestController("ApiContentPostController")
@AllArgsConstructor
@RequestMapping("/api/content/post")
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    /**
     * 首页文章列表
     *
     * @param param param
     * @return PageResult
     */
    @GetMapping
    public PageResult<PostListDTO> page(PostQueryParam param) {
        // 只要发布状态的文章
        param.setStatus(PostStatus.PUBLISHED);
        return postService.page(param);
    }

    /**
     * 用树状视图列出评论
     *
     * @param postId postId
     * @return PageResult
     */
    @GetMapping("{postId:\\d+}/comment/tree_view")
    public PageResult<CommentDTO> listCommentsTree(@PathVariable("postId") Integer postId, CommentQueryParam param) {
        param.setType(CommentType.POST);
        param.setStatus(CommentStatus.PUBLISHED);
        return commentService.pageTree(postId, param);
    }


    /**
     * 新增评论
     *
     * @param param param
     * @return Boolean
     */
    @PostMapping("comment")
    @CacheLock(autoDelete = false, traceRequest = true)
    public Boolean comment(@RequestBody CommentParam param) {
        commentService.validateCommentBlackListStatus();
        param.setType(CommentType.POST);
        // 转义内容
        param.setContent(HtmlUtils.htmlEscape(param.getContent(), StandardCharsets.UTF_8.displayName()));
        return commentService.save(param);
    }


    /**
     * 点赞
     *
     * @param postId postId
     * @return Boolean
     */
    @PostMapping("{postId:\\d+}/likes")
    public Boolean like(@PathVariable("postId") Integer postId) {
        return postService.increaseLike(postId);
    }

}
