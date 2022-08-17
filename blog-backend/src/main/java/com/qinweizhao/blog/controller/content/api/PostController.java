package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内容发布控制器
 *
 * @author johnniang
 * @author qinweizhao
 * @since 2019-04-02
 */
@RestController("ApiContentPostController")
@AllArgsConstructor
@RequestMapping("/api/content/posts")
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
    @GetMapping("{postId:\\d+}/comments/tree_view")
    public PageResult<CommentDTO> listCommentsTree(@PathVariable("postId") Integer postId, CommentQueryParam param) {
        param.setType(CommentType.POST);
        return commentService.pageTree(postId, param);
    }


}
