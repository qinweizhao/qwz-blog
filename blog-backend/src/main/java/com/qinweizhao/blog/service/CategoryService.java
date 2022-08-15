package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.param.CategoryParam;

import java.util.List;

/**
 * Category service.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-14
 */
public interface CategoryService {

    /**
     * get
     *
     * @param categoryId categoryId
     * @return CategoryDTO
     */
    CategoryDTO getById(Integer categoryId);

    /**
     * 通过别名获取分类
     * @param slug slug
     * @return CategoryDTO
     */
    CategoryDTO getBySlug(String slug);

    /**
     * 列表(菜单)
     *
     * @return List
     * @param more more
     */
    List<CategoryDTO> list(boolean more);


    /**
     * 将所有分类列为树
     *
     * @return List
     */
    List<CategoryDTO> listAsTree();

    /**
     * 新增分类
     *
     * @param categoryParam categoryParam
     * @return boolean
     */
    boolean saveCategory(CategoryParam categoryParam);

    /**
     * 更新分类
     *
     * @param categoryId    categoryId
     * @param categoryParam categoryParam
     * @return boolean
     */
    boolean updateById(Integer categoryId, CategoryParam categoryParam);

    /**
     * 移除分类
     *
     * @param categoryId categoryId
     * @return boolean
     */
    boolean removeCategoryAndPostCategoryById(Integer categoryId);

    /**
     * 统计分类个数
     *
     * @return Long
     */
    Long count();

    /**
     * 批量更新
     *
     * @param params params
     * @return boolean
     */
    boolean updateInBatch(List<CategoryParam> params);

}
