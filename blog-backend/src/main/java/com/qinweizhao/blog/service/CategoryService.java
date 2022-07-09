package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.vo.CategoryVO;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Category service.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
@Transactional(readOnly = true)
public interface CategoryService extends IService<Category> {


    /**
     * 列表
     * @return List
     */
    @Override
    List<Category> list();


    /**
     * Lists as category tree.
     *
     * @param sort sort info must not be null
     * @return a category tree
     */
    List<CategoryVO> listAsTree(Sort sort);

    /**
     * Get category by slug
     *
     * @param slug slug
     * @return Category
     */
    Category getBySlug(String slug);


    /**
     * Get Category by name.
     *
     * @param name name
     * @return Category
     */
    @Nullable
    Category getByName(String name);

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
    List<Category> listByParentId(Integer id);

    /**
     * 保存分类
     * @param category category
     * @return boolean
     */
    boolean saveCategory(Category category);
}
