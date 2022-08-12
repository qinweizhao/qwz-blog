//package com.qinweizhao.blog.controller.content.model;
//
//import com.qinweizhao.blog.model.dto.TagDTO;
//import com.qinweizhao.blog.model.entity.Post;
//import com.qinweizhao.blog.model.enums.PostStatus;
//import com.qinweizhao.blog.model.vo.PostListVO;
//import com.qinweizhao.blog.service.*;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.Model;
//
//import javax.annotation.Resource;
//
//import static org.springframework.data.domain.Sort.Direction.DESC;
//
///**
// * Tag Model.
// *
// * @author ryanwang
// * @author qinweizhao
// * @since 2020-01-11
// */
//@Component
//public class TagModel {
//
//    @Resource
//    private TagService tagService;
//
//    @Resource
//    private PostService postService;
//
//    @Resource
//    private PostTagService postTagService;
//
//    @Resource
//    private OptionService optionService;
//
//    @Resource
//    private ThemeService themeService;
//
//
//    public String list(Model model) {
//        model.addAttribute("is_tags", true);
//        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
//        model.addAttribute("meta_description", optionService.getSeoDescription());
//        return themeService.render("tags");
//    }
//
//    public String listPost(Model model, String slug, Integer page) {
//        // Get tag by slug
//        final Tag tag = tagService.getBySlugOfNonNull(slug);
//        TagDTO tagDTO = tagService.convertTo(tag);
//
//        final Pageable pageable = PageRequest.of(page - 1, optionService.getArchivesPageSize(), Sort.by(DESC, "createTime"));
//        Page<Post> postPage = postTagService.pagePostsBy(tag.getId(), PostStatus.PUBLISHED, pageable);
//        Page<PostListVO> posts = postService.convertToListVo(postPage);
//
//        model.addAttribute("is_tag", true);
//        model.addAttribute("posts", posts);
//        model.addAttribute("tag", tagDTO);
//        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
//        model.addAttribute("meta_description", optionService.getSeoDescription());
//        return themeService.render("tag");
//    }
//}
