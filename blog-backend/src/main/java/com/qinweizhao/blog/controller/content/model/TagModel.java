package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ArticleListDTO;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.ArticleService;
import com.qinweizhao.blog.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;


/**
 * Tag Model.
 *
 * @author qinweizhao
 * @since 2020-01-11
 */
@Component
@AllArgsConstructor
public class TagModel {

    private final TagService tagService;

    private final ArticleService articleService;

    private final SettingService settingService;

    /**
     * tags.html
     *
     * @param model model
     * @return String
     */
    public String list(Model model) {
        model.addAttribute("is_tags", true);
        model.addAttribute("meta_keywords", settingService.get("seo_keywords"));
        model.addAttribute("meta_description", settingService.get("seo_description"));
        return settingService.render("tags");
    }

    /**
     * tags/xxx.html
     *
     * @param model model
     * @param slug  slug
     * @param page  page
     * @return String
     */
    public String listPost(Model model, String slug, Integer page) {

        final TagDTO tagDTO = tagService.getBySlug(slug);
        int pageSize = settingService.getPostPageSize();

        ArticleQueryParam param = new ArticleQueryParam();
        param.setPage(page);
        param.setSize(pageSize);
        param.setStatus(ArticleStatus.PUBLISHED);
        param.setTagId(tagDTO.getId());
        PageResult<ArticleListDTO> posts = articleService.page(param);

        model.addAttribute("is_tag", true);
        model.addAttribute("posts", posts);
        model.addAttribute("tag", tagDTO);
        model.addAttribute("meta_keywords", settingService.get("seo_keywords"));
        model.addAttribute("meta_description", settingService.get("seo_description"));
        return settingService.render("tag");
    }
}
