package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.convert.CategoryConvert;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.params.CategoryParam;
import com.qinweizhao.blog.model.vo.CategoryVO;
import com.qinweizhao.blog.service.CategoryService;
import com.qinweizhao.blog.utils.ResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

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


    /**
     * 获取分类详细信息
     *
     * @param categoryId categoryId
     * @return CategoryDTO
     */
    @GetMapping("{categoryId:\\d+}")
    public CategoryDTO getBy(@PathVariable("categoryId") Integer categoryId) {
        return CategoryConvert.INSTANCE.convert(categoryService.getById(categoryId));
    }

    /**
     * 分类列表
     *
     * @return List
     */
    @GetMapping
    public List<? extends CategoryDTO> listAll() {
        return CategoryConvert.INSTANCE.convertToDTO(categoryService.list());
    }

    @GetMapping("tree_view")
    @ApiOperation("将所有分类列为树")
    public List<CategoryVO> listAsTree(@SortDefault(sort = "name", direction = ASC) Sort sort) {
        return categoryService.listAsTree(sort);
    }

    @PostMapping
    @ApiOperation("新增分类")
    public CategoryDTO createBy(@RequestBody @Valid CategoryParam categoryParam) {

        Category category = CategoryConvert.INSTANCE.convert(categoryParam);
        boolean b = categoryService.saveCategory(category);
        return ResultUtils.judge(b, new CategoryDTO());
    }

    @PutMapping("{categoryId:\\d+}")
    @ApiOperation("更新分类")
    public CategoryDTO updateBy(@PathVariable("categoryId") Integer categoryId,
                                @RequestBody @Valid CategoryParam categoryParam) {
        Category category = CategoryConvert.INSTANCE.convert(categoryParam);
        category.setId(categoryId);
        boolean b = categoryService.updateById(category);
        CategoryDTO resultCategory = CategoryConvert.INSTANCE.convert(category);
        return ResultUtils.judge(b, resultCategory);
    }

    @DeleteMapping("{categoryId:\\d+}")
    @ApiOperation("删除分类")
    public void deletePermanently(@PathVariable("categoryId") Integer categoryId) {
        categoryService.removeCategoryAndPostCategoryBy(categoryId);
    }
}
