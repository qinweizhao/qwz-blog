package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.mapper.CommentMapper;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostDTO;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.PostService;
import com.qinweizhao.blog.service.ThemeService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
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

    @Resource
    private PostService postService;

    @Resource
    private ThemeService themeService;


    private final OptionService optionService;

    private final AbstractStringCacheStore cacheStore;

    private final CommentMapper commentMapper;


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


        if (StringUtils.isNotEmpty(post.getMetaKeywords())) {
            model.addAttribute("meta_keywords", post.getMetaKeywords());
        } else {
            List<TagDTO> tags = post.getTags();
            model.addAttribute("meta_keywords", tags.stream().map(TagDTO::getName).collect(Collectors.joining(",")));
        }

        if (StringUtils.isNotEmpty(post.getMetaDescription())) {
            model.addAttribute("meta_description", post.getMetaDescription());
        } else {
            model.addAttribute("meta_description", postService.generateDescription(post.getFormatContent()));
        }

//        model.addAttribute("is_post", true);
        model.addAttribute("post", post);
//        model.addAttribute("categories", post.getCategories());
//        model.addAttribute("tags", tags);
//        model.addAttribute("metas", post.getMetas());

        // 发送事件
        postService.publishVisitEvent(post.getId());


        return themeService.render("post");
    }

    public String list(Integer page, Model model) {
        int pageSize = optionService.getPostPageSize();

        PostQueryParam param = new PostQueryParam();
        param.setSize(pageSize);
        param.setPage(page);
        PageResult<PostSimpleDTO> posts = postService.pageSimple(param);
//        PageResult<PostListDTO> posts = postService.page(param);

        model.addAttribute("is_index", true);
        model.addAttribute("posts", posts);
        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
        model.addAttribute("meta_description", optionService.getSeoDescription());
        return themeService.render("index");
    }
//
//    public String archives(Integer page, Model model) {
//        int pageSize = optionService.getArchivesPageSize();
//        Pageable pageable = PageRequest
//                .of(page >= 1 ? page - 1 : page, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
//
//        Page<Post> postPage = postService.pageBy(PostStatus.PUBLISHED, pageable);
//
//        Page<PostListVO> posts = postService.convertToListVo(postPage);
//
//        List<ArchiveYearVO> archives = postService.convertToYearArchives(postPage.getContent());
//
//        model.addAttribute("is_archives", true);
//        model.addAttribute("posts", posts);
//        model.addAttribute("archives", archives);
//        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
//        model.addAttribute("meta_description", optionService.getSeoDescription());
//        return themeService.render("archives");
//    }
}
