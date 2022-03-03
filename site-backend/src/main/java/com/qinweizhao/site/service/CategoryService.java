package com.qinweizhao.site.service;

import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import com.qinweizhao.site.model.dto.CategoryDTO;
import com.qinweizhao.site.model.entity.Category;
import com.qinweizhao.site.model.vo.CategoryVO;
import com.qinweizhao.site.service.base.CrudService;

import java.util.List;

/**
 * Category service.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
@Transactional(readOnly = true)
public interface CategoryService extends CrudService<Category, Integer> {

    /**
     * Lists as category tree.
     *
     * @param sort sort info must not be null
     * @return a category tree
     */
    @NonNull
    List<CategoryVO> listAsTree(@NonNull Sort sort);

    /**
     * Get category by slug
     *
     * @param slug slug
     * @return Category
     */
    @NonNull
    Category getBySlug(@NonNull String slug);

    /**
     * Get category by slug
     *
     * @param slug slug
     * @return Category
     */
    @NonNull
    Category getBySlugOfNonNull(String slug);

    /**
     * Get Category by name.
     *
     * @param name name
     * @return Category
     */
    @Nullable
    Category getByName(@NonNull String name);

    /**
     * Removes category and post categories.
     *
     * @param categoryId category id must not be null
     */
    @Transactional
    void removeCategoryAndPostCategoryBy(Integer categoryId);

    /**
     * List categories by parent id.
     *
     * @param id parent id.
     * @return list of category.
     */
    List<Category> listByParentId(@NonNull Integer id);

    /**
     * Converts to category dto.
     *
     * @param category category must not be null
     * @return category dto
     */
    @NonNull
    CategoryDTO convertTo(@NonNull Category category);

    /**
     * Converts to category dto list.
     *
     * @param categories category list
     * @return a list of category dto
     */
    @NonNull
    List<CategoryDTO> convertTo(@Nullable List<Category> categories);
}
