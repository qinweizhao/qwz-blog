package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.dto.CategoryWithPostCountDTO;
import com.qinweizhao.blog.model.param.CategoryParam;
import com.qinweizhao.blog.service.CategoryService;
import com.qinweizhao.blog.service.PostCategoryService;
import com.qinweizhao.blog.util.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Category controller.
 *
 * @author johnniang
 * @date 2019-03-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    private final PostCategoryService postCategoryService;

    /**
     * 获取分类详细信息
     *
     * @param categoryId categoryId
     * @return CategoryDTO
     */
    @GetMapping("{categoryId:\\d+}")
    public CategoryDTO get(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.getById(categoryId);
    }

    /**
     * 分类列表
     *
     * @return List
     */
    @GetMapping
    public List<CategoryWithPostCountDTO> list() {
        return postCategoryService.listCategoryWithPostCountDto();
    }

    /**
     * 将所有分类列为树
     *
     * @return List
     */
    @GetMapping("tree_view")
    public List<CategoryDTO> listAsTree() {
        return categoryService.listAsTree();
    }

    /**
     * 新增分类
     *
     * @param categoryParam categoryParam
     * @return Boolean
     */
    @PostMapping
    public Boolean saveCategory(@RequestBody @Valid CategoryParam categoryParam) {
        return categoryService.saveCategory(categoryParam);
    }

    /**
     * 更新分类
     *
     * @param categoryId    category Id
     * @param categoryParam categoryId
     * @return CategoryDTO
     */
    @PutMapping("{categoryId:\\d+}")
    public Boolean update(@PathVariable("categoryId") Integer categoryId,
                            @RequestBody @Valid CategoryParam categoryParam) {

        return categoryService.updateById(categoryId, categoryParam);
    }

    /**
     * 删除分类
     *
     * @param categoryId categoryId
     */
    @DeleteMapping("{categoryId:\\d+}")
    public void deletePermanently(@PathVariable("categoryId") Integer categoryId) {
        categoryService.removeCategoryAndPostCategoryById(categoryId);
    }

}
