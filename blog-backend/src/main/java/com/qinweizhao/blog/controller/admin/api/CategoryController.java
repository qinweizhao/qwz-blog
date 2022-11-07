package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.param.CategoryParam;
import com.qinweizhao.blog.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Category controller.
 *
 * @author johnniang
 * @since 2019-03-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/category")
public class CategoryController {

    private final CategoryService categoryService;

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
    public List<CategoryDTO> list(@RequestParam(name = "more", required = false, defaultValue = "false") boolean more) {
        return categoryService.list(more);
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
    public Boolean saveCategory(@RequestBody @Validated CategoryParam categoryParam) {
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
    public Boolean update(@PathVariable("categoryId") Integer categoryId, @RequestBody @Valid CategoryParam categoryParam) {
        return categoryService.updateById(categoryId, categoryParam);
    }

    /**
     * 批量更新分类
     *
     * @return Boolean
     */
    @PutMapping("/batch")
    public Boolean updateBatch(@RequestBody List<@Valid CategoryParam> params) {
        return categoryService.updateInBatch(params);
    }

    /**
     * 删除分类
     *
     * @param categoryId categoryId
     * @return Boolean
     */
    @DeleteMapping("{categoryId:\\d+}")
    public Boolean deletePermanently(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.removeCategoryAndPostCategoryById(categoryId);
    }


}
