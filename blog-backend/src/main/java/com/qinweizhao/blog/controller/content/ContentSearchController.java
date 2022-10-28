package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostService;
import com.qinweizhao.blog.service.ThemeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;


/**
 * Search controller.
 *
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-04-21
 */
@Controller
@RequestMapping(value = "/search")
public class ContentSearchController {

    @Resource
    private PostService postService;

    @Resource
    private ConfigService configService;

    @Resource
    private ThemeService themeService;


    /**
     * 渲染文章搜索页面
     *
     * @param model   model
     * @param keyword keyword
     * @return template path : themes/{theme}/search.ftl
     */
    @GetMapping
    public String search(Model model, @RequestParam(value = "keyword") String keyword, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {

        final PostQueryParam param = new PostQueryParam();
        param.setPage(page);
        param.setKeyword(keyword);
        param.setStatus(PostStatus.PUBLISHED);
        final PageResult<PostListDTO> postPage = postService.page(param);

        model.addAttribute("is_search", true);
        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", postPage);
        model.addAttribute("meta_keywords", configService.getSeoKeywords());
        model.addAttribute("meta_description", configService.getSeoDescription());
        return themeService.render("search");
    }

}
