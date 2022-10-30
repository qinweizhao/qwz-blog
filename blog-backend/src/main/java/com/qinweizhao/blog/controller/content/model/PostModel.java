package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostDTO;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostService;
import com.qinweizhao.blog.service.ThemeService;
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
public class PostModel {

    private final PostService postService;

    private final ThemeService themeService;

    private final ConfigService configService;

    private final AbstractStringCacheStore cacheStore;


    public String content(Integer postId, PostStatus status, String token, Model model) {

        if (PostStatus.RECYCLE.equals(status)) {
            throw new NotFoundException("查询不到该文章的信息");
        }

        if (PostStatus.DRAFT.equals(status) && StringUtils.isEmpty(token)) {
            throw new NotFoundException("查询不到该文章的信息");
        }

        if (PostStatus.DRAFT.equals(status) && StringUtils.isNotEmpty(token)) {
            // 验证 token
            String cachedToken = cacheStore.getAny(token, String.class).orElseThrow(() -> new ForbiddenException("您没有该文章的访问权限"));
            if (!cachedToken.equals(token)) {
                throw new ForbiddenException("预览文章携带的令牌错误");
            }
        }

        PostDTO post = postService.getById(postId);

        model.addAttribute("prevPost", postService.getPrevPost(postId));
        model.addAttribute("nextPost", postService.getNextPost(postId));

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
            metaDescription = postService.generateDescription(post.getFormatContent());
        }

        model.addAttribute("meta_keywords", metaKeywords);
        model.addAttribute("meta_description", metaDescription);

        model.addAttribute("is_post", true);
        model.addAttribute("post", post);

        // 发送事件
        postService.publishVisitEvent(post.getId());

        return themeService.render("post");
    }

    public String list(Integer page, Model model) {
        int pageSize = configService.getPostPageSize();

        PostQueryParam param = new PostQueryParam();
        param.setSize(pageSize);
        param.setPage(page);
        PageResult<PostListDTO> posts = postService.page(param);

        model.addAttribute("is_index", true);
        model.addAttribute("posts", posts);
        model.addAttribute("meta_keywords", configService.getSeoKeywords());
        model.addAttribute("meta_description", configService.getSeoDescription());
        return themeService.render("index");
    }

    public String archives(Model model) {
        model.addAttribute("is_archives", true);
        model.addAttribute("meta_keywords", configService.getSeoKeywords());
        model.addAttribute("meta_description", configService.getSeoDescription());
        return themeService.render("archives");
    }


}
