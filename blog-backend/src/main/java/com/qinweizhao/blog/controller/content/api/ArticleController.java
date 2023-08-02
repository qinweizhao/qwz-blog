package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ArticleListDTO;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
import com.qinweizhao.blog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台-文章
 *
 * @author qinweizhao
 * @since 2022-03-15
 */
@RestController("ApiContentPostController")
@AllArgsConstructor
@RequestMapping("/api/content/post")
public class ArticleController {

    private final ArticleService articleService;


    /**
     * 首页文章列表
     *
     * @param param param
     * @return PageResult
     */
    @GetMapping
    public PageResult<ArticleListDTO> page(ArticleQueryParam param) {
        // 只要发布状态的文章
        param.setStatus(ArticleStatus.PUBLISHED);
        return articleService.page(param);
    }

}
