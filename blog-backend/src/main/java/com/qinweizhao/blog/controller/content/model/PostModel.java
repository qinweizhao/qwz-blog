package com.qinweizhao.blog.controller.content.model;

import org.springframework.stereotype.Component;

/**
 * Post Model
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2020-01-07
 */
@Component
public class PostModel {

//    @Resource
//    private PostService postService;
//
//    @Resource
//    private ThemeService themeService;
//
//    @Resource
//    private PostCategoryService postCategoryService;
//
//    @Resource
//    private CategoryService categoryService;
//
//    @Resource
//    private PostTagService postTagService;
//
//    @Resource
//    private TagService tagService;
//
//    @Resource
//    private PostMetaService postMetaService;
//
//    @Resource
//    private OptionService optionService;
//
//    @Resource
//    private AbstractStringCacheStore cacheStore;
//
//
//    public String content(Post post, String token, Model model) {
//
//        if (post.getStatus().equals(PostStatus.INTIMATE) && StringUtils.isEmpty(token)) {
//            model.addAttribute("slug", post.getSlug());
//            return "common/template/post_password";
//        }
//
//        if (StringUtils.isEmpty(token)) {
//            post = postService.getBy(PostStatus.PUBLISHED, post.getSlug());
//        } else {
//            // verify token
//            String cachedToken = cacheStore.getAny(token, String.class).orElseThrow(() -> new ForbiddenException("您没有该文章的访问权限"));
//            if (!cachedToken.equals(token)) {
//                throw new ForbiddenException("您没有该文章的访问权限");
//            }
//            if (post.getEditorType().equals(PostEditorType.MARKDOWN)) {
//                post.setFormatContent(MarkdownUtils.renderHtml(post.getOriginalContent()));
//            } else {
//                post.setFormatContent(post.getOriginalContent());
//            }
//        }
//
//        postService.publishVisitEvent(post.getId());
//
//        postService.getPrevPost(post).ifPresent(prevPost -> model.addAttribute("prevPost", postService.convertToDetailVo(prevPost)));
//        postService.getNextPost(post).ifPresent(nextPost -> model.addAttribute("nextPost", postService.convertToDetailVo(nextPost)));
//
//        List<Category> categories = postCategoryService.listCategoriesBy(post.getId());
//        List<Tag> tags = postTagService.listTagsBy(post.getId());
//        List<PostMeta> metas = postMetaService.listBy(post.getId());
//
//        // Generate meta keywords.
//        if (StringUtils.isNotEmpty(post.getMetaKeywords())) {
//            model.addAttribute("meta_keywords", post.getMetaKeywords());
//        } else {
//            model.addAttribute("meta_keywords", tags.stream().map(Tag::getName).collect(Collectors.joining(",")));
//        }
//
//        // Generate meta description.
//        if (StringUtils.isNotEmpty(post.getMetaDescription())) {
//            model.addAttribute("meta_description", post.getMetaDescription());
//        } else {
//            model.addAttribute("meta_description", postService.generateDescription(post.getFormatContent()));
//        }
//
//        model.addAttribute("is_post", true);
//        model.addAttribute("post", postService.convertToDetailVo(post));
//        model.addAttribute("categories", categoryService.convertTo(categories));
//        model.addAttribute("tags", tagService.convertTo(tags));
//        model.addAttribute("metas", postMetaService.convertToMap(metas));
//
//        if (themeService.templateExists(
//                ThemeService.CUSTOM_POST_PREFIX + post.getTemplate() + HaloConst.SUFFIX_FTL)) {
//            return themeService.render(ThemeService.CUSTOM_POST_PREFIX + post.getTemplate());
//        }
//
//        return themeService.render("post");
//    }
//
//    public String list(Integer page, Model model) {
//        int pageSize = optionService.getPostPageSize();
//        Pageable pageable = PageRequest
//                .of(page >= 1 ? page - 1 : page, pageSize, postService.getPostDefaultSort());
//
//        Page<Post> postPage = postService.pageBy(PostStatus.PUBLISHED, pageable);
//        Page<PostListVO> posts = postService.convertToListVo(postPage);
//
//        model.addAttribute("is_index", true);
//        model.addAttribute("posts", posts);
//        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
//        model.addAttribute("meta_description", optionService.getSeoDescription());
//        return themeService.render("index");
//    }
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
