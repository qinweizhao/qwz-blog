package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.PostService;
import com.qinweizhao.blog.service.ThemeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

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
    private OptionService optionService;

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
    public String search(Model model, @RequestParam(value = "keyword") String keyword) {
        return this.search(model, HtmlUtils.htmlEscape(keyword), 1);
    }

    /**
     * 渲染文章搜索页面
     *
     * @param model   model
     * @param keyword keyword
     * @return template path :themes/{theme}/search.ftl
     */
    @GetMapping(value = "page/{page}")
    public String search(Model model, @RequestParam(value = "keyword") String keyword, @PathVariable(value = "page") Integer page) {

        final PostQueryParam param = new PostQueryParam();
        param.setPage(page);
        param.setKeyword(keyword);
        param.setStatus(PostStatus.PUBLISHED);
        final PageResult<PostSimpleDTO> postPage = postService.pageSimple(param);


        model.addAttribute("is_search", true);
        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", postPage);
        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
        model.addAttribute("meta_description", optionService.getSeoDescription());
        return themeService.render("search");
    }
}
