package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ArticleListDTO;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Search controller.
 *
 * @author qinweizhao
 * @since 2019-04-21
 */
@Controller
@AllArgsConstructor
public class ContentSearchController {

    private final ArticleService articleService;

    private final SettingService settingService;


    /**
     * 渲染文章搜索页面
     *
     * @param model   model
     * @param keyword keyword
     * @return template path : themes/{theme}/search.ftl
     */
    @GetMapping(value = "/search")
    public String search(Model model, @RequestParam(value = "keyword") String keyword, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {

        final ArticleQueryParam param = new ArticleQueryParam();
        param.setPage(page);
        param.setKeyword(keyword);
        param.setStatus(ArticleStatus.PUBLISHED);
        final PageResult<ArticleListDTO> postPage = articleService.page(param);

        model.addAttribute("is_search", true);
        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", postPage);
        model.addAttribute("meta_keywords", settingService.get("seo_keywords"));
        model.addAttribute("meta_description", settingService.get("seo_description"));
        return settingService.render("search");
    }

}
