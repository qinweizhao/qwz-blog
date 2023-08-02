package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ArticleListDTO;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
import com.qinweizhao.blog.service.CategoryService;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.annotation.Resource;

/**
 * Category Model.
 *
 * @author qinweizhao
 * @since 2020-01-11
 */
@Component
public class CategoryModel {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleService articleService;

    @Resource
    private SettingService settingService;


    /**
     * List categories.
     *
     * @param model model
     * @return template name
     */
    public String list(Model model) {
        model.addAttribute("is_categories", true);
        model.addAttribute("meta_keywords", settingService.get("seo_keywords"));
        model.addAttribute("meta_description", settingService.get("seo_description"));
        return settingService.render("categories");
    }

    /**
     * List category posts.
     *
     * @param model model
     * @param slug  slug
     * @param page  current page
     * @return template name
     */
    public String listPost(Model model, String slug, Integer page) {

        int pageSize = settingService.getPostPageSize();

        CategoryDTO categoryDTO = categoryService.getBySlug(slug);

        ArticleQueryParam param = new ArticleQueryParam();
        param.setSize(pageSize);
        param.setPage(page);
        param.setStatus(ArticleStatus.PUBLISHED);
        param.setCategoryId(categoryDTO.getId());

        PageResult<ArticleListDTO> posts = articleService.page(param);

        // Generate meta description.
        if (StringUtils.isNotEmpty(categoryDTO.getDescription())) {
            model.addAttribute("meta_description", categoryDTO.getDescription());
        } else {
            model.addAttribute("meta_description", settingService.get("seo_description"));
        }

        model.addAttribute("is_category", true);
        model.addAttribute("posts", posts);
        model.addAttribute("category", categoryDTO);
        model.addAttribute("meta_keywords", settingService.get("seo_keywords"));
        return settingService.render("category");
    }
}
