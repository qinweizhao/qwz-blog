package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostService;
import com.qinweizhao.blog.service.TagService;
import com.qinweizhao.blog.service.ThemeService;
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

    private final PostService postService;

    private final ConfigService configService;

    private final ThemeService themeService;

    /**
     * tags.html
     *
     * @param model model
     * @return String
     */
    public String list(Model model) {
        model.addAttribute("is_tags", true);
        model.addAttribute("meta_keywords", configService.get("seo_keywords"));
        model.addAttribute("meta_description", configService.get("seo_description"));
        return themeService.render("tags");
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
        int pageSize = configService.getPostPageSize();

        PostQueryParam param = new PostQueryParam();
        param.setPage(page);
        param.setSize(pageSize);
        param.setStatus(PostStatus.PUBLISHED);
        param.setTagId(tagDTO.getId());
        PageResult<PostListDTO> posts = postService.page(param);

        model.addAttribute("is_tag", true);
        model.addAttribute("posts", posts);
        model.addAttribute("tag", tagDTO);
        model.addAttribute("meta_keywords", configService.get("seo_keywords"));
        model.addAttribute("meta_description", configService.get("seo_description"));
        return themeService.render("tag");
    }
}
