package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 前台-文章
 *
 * @author qinweizhao
 * @since 2022-03-15
 */
@RestController("ApiContentPostController")
@AllArgsConstructor
@RequestMapping("/api/content/post")
public class PostController {

    private final PostService postService;


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

}
