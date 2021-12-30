package com.qinweizhao.site.controller.content.api;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.google.common.collect.Sets;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.qinweizhao.site.exception.ForbiddenException;
import com.qinweizhao.site.model.dto.CategoryDTO;
import com.qinweizhao.site.model.entity.Category;
import com.qinweizhao.site.model.entity.Post;
import com.qinweizhao.site.model.enums.PostStatus;
import com.qinweizhao.site.model.vo.PostListVO;
import com.qinweizhao.site.service.AuthenticationService;
import com.qinweizhao.site.service.CategoryService;
import com.qinweizhao.site.service.PostCategoryService;
import com.qinweizhao.site.service.PostService;

/**
 * Content category controller.
 *
 * @author ryanwang
 * @date 2019-06-09
 */
@RestController("ApiContentCategoryController")
@RequestMapping("/api/content/categories")
public class CategoryController {

    private final CategoryService categoryService;

    private final PostCategoryService postCategoryService;

    private final PostService postService;

    private final AuthenticationService authenticationService;

    public CategoryController(CategoryService categoryService,
        PostCategoryService postCategoryService,
        PostService postService,
        AuthenticationService authenticationService) {
        this.categoryService = categoryService;
        this.postCategoryService = postCategoryService;
        this.postService = postService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    @ApiOperation("Lists categories")
    public List<? extends CategoryDTO> listCategories(
        @SortDefault(sort = "updateTime", direction = DESC) Sort sort,
        @RequestParam(name = "more", required = false, defaultValue = "false") Boolean more) {
        if (more) {
            return postCategoryService.listCategoryWithPostCountDto(sort, false);
        }
        return categoryService.convertTo(categoryService.listAll(sort));
    }

    @GetMapping("{slug}/posts")
    @ApiOperation("Lists posts by category slug")
    public Page<PostListVO> listPostsBy(@PathVariable("slug") String slug,
        @RequestParam(value = "password", required = false) String password,
        @PageableDefault(sort = {"topPriority", "updateTime"}, direction = DESC)
            Pageable pageable) {
        // Get category by slug
        Category category = categoryService.getBySlugOfNonNull(slug, true);

        if (!authenticationService.categoryAuthentication(category.getId(), password)) {
            throw new ForbiddenException("您没有该分类的访问权限");
        }

        Page<Post> postPage =
            postCategoryService.pagePostBy(category.getId(),
                Sets.immutableEnumSet(PostStatus.PUBLISHED), pageable);
        return postService.convertToListVo(postPage);
    }
}
