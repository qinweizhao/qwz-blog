package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ArticleDTO;
import com.qinweizhao.blog.model.dto.ArticleListDTO;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Post Model
 *
 * @author qinweizhao
 * @since 2020-01-07
 */
@Component
@AllArgsConstructor
public class ArticleModel {

    private final ArticleService articleService;


    private final SettingService settingService;

    private final AbstractStringCacheStore cacheStore;


    public String content(Integer articleId, ArticleStatus status, String token, Model model) {

        if (ArticleStatus.RECYCLE.equals(status)) {
            throw new NotFoundException("查询不到该文章的信息");
        }

        if (ArticleStatus.DRAFT.equals(status) && StringUtils.isEmpty(token)) {
            throw new NotFoundException("查询不到该文章的信息");
        }

        if (ArticleStatus.DRAFT.equals(status) && StringUtils.isNotEmpty(token)) {
            // 验证 token
            String cachedToken = cacheStore.getAny(token, String.class).orElseThrow(() -> new ForbiddenException("您没有该文章的访问权限"));
            if (!cachedToken.equals(token)) {
                throw new ForbiddenException("预览文章携带的令牌错误");
            }
        }

        ArticleDTO post = articleService.getById(articleId);

        model.addAttribute("prevPost", articleService.getPrevPost(articleId));
        model.addAttribute("nextPost", articleService.getNextPost(articleId));

        String metaKeywords;

        if (StringUtils.isNotEmpty(post.getMetaKeywords())) {
            metaKeywords = post.getMetaKeywords();
        } else {
            List<TagDTO> tags = post.getTags();
            metaKeywords = tags.stream().map(TagDTO::getName).collect(Collectors.joining(","));
        }

        String metaDescription;

        if (StringUtils.isNotEmpty(post.getMetaDescription())) {
            metaDescription = post.getMetaDescription();
        } else {
            metaDescription = articleService.generateDescription(post.getFormatContent());
        }

        model.addAttribute("meta_keywords", metaKeywords);
        model.addAttribute("meta_description", metaDescription);

        model.addAttribute("is_post", true);
        model.addAttribute("post", post);

        // 发送事件
        articleService.publishVisitEvent(post.getId());

        return settingService.render("post");
    }

    public String list(Integer page, Model model) {
        int pageSize = settingService.getPostPageSize();

        ArticleQueryParam param = new ArticleQueryParam();
        param.setSize(pageSize);
        param.setPage(page);
        PageResult<ArticleListDTO> posts = articleService.page(param);

        model.addAttribute("is_index", true);
        model.addAttribute("posts", posts);
        model.addAttribute("meta_keywords", settingService.get("seo_keywords"));
        model.addAttribute("meta_description", settingService.get("seo_description"));
        return settingService.render("index");
    }

    public String archives(Model model) {
        model.addAttribute("is_archives", true);
        model.addAttribute("meta_keywords", settingService.get("seo_keywords"));
        model.addAttribute("meta_description", settingService.get("seo_description"));
        return settingService.render("archives");
    }


}
