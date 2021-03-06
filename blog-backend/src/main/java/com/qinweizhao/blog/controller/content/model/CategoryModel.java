package com.qinweizhao.blog.controller.content.model;

import org.springframework.stereotype.Component;

/**
 * Category Model.
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2020-01-11
 */
@Component
public class CategoryModel {

//    @Resource
//    private CategoryService categoryService;
//
//    @Resource
//    private ThemeService themeService;
//
//    @Resource
//    private PostCategoryService postCategoryService;
//
//    @Resource
//    private PostService postService;
//
//    @Resource
//    private OptionService optionService;
//
//
//    /**
//     * List categories.
//     *
//     * @param model model
//     * @return template name
//     */
//    public String list(Model model) {
//        model.addAttribute("is_categories", true);
//        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
//        model.addAttribute("meta_description", optionService.getSeoDescription());
//        return themeService.render("categories");
//    }
//
//    /**
//     * List category posts.
//     *
//     * @param model model
//     * @param slug  slug
//     * @param page  current page
//     * @return template name
//     */
//    public String listPost(Model model, String slug, Integer page) {
//        // Get category by slug
//        final Category category = categoryService.getBySlugOfNonNull(slug);
//        CategoryDTO categoryDTO = categoryService.convertTo(category);
//
//        final Pageable pageable = PageRequest.of(page - 1,
//                optionService.getArchivesPageSize(),
//                Sort.by(DESC, "topPriority", "createTime"));
//        Page<Post> postPage = postCategoryService.pagePostBy(category.getId(), PostStatus.PUBLISHED, pageable);
//        Page<PostListVO> posts = postService.convertToListVo(postPage);
//
//        // Generate meta description.
//        if (StringUtils.isNotEmpty(category.getDescription())) {
//            model.addAttribute("meta_description", category.getDescription());
//        } else {
//            model.addAttribute("meta_description", optionService.getSeoDescription());
//        }
//
//        model.addAttribute("is_category", true);
//        model.addAttribute("posts", posts);
//        model.addAttribute("category", categoryDTO);
//        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
//        return themeService.render("category");
//    }
}
