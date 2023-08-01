package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.CategoryService;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostService;
import com.qinweizhao.blog.service.ThemeService;
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
    private ThemeService themeService;

    @Resource
    private PostService postService;

    @Resource
    private ConfigService configService;


    /**
     * List categories.
     *
     * @param model model
     * @return template name
     */
    public String list(Model model) {
        model.addAttribute("is_categories", true);
        model.addAttribute("meta_keywords", configService.get("seo_keywords"));
        model.addAttribute("meta_description", configService.get("seo_description"));
        return themeService.render("categories");
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

        int pageSize = configService.getPostPageSize();

        CategoryDTO categoryDTO = categoryService.getBySlug(slug);

        PostQueryParam param = new PostQueryParam();
        param.setSize(pageSize);
        param.setPage(page);
        param.setStatus(PostStatus.PUBLISHED);
        param.setCategoryId(categoryDTO.getId());

        PageResult<PostListDTO> posts = postService.page(param);

        // Generate meta description.
        if (StringUtils.isNotEmpty(categoryDTO.getDescription())) {
            model.addAttribute("meta_description", categoryDTO.getDescription());
        } else {
            model.addAttribute("meta_description", configService.get("seo_description"));
        }

        model.addAttribute("is_category", true);
        model.addAttribute("posts", posts);
        model.addAttribute("category", categoryDTO);
        model.addAttribute("meta_keywords", configService.get("seo_keywords"));
        return themeService.render("category");
    }
}
