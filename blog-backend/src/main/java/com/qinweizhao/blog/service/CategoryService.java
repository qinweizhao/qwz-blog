package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.dto.CategoryWithPostCountDTO;
import com.qinweizhao.blog.model.params.CategoryParam;
import com.qinweizhao.blog.model.vo.CategoryVO;

import java.util.List;

/**
 * Category service.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-14
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
     * 列表
     *
     * @return List
     */
    List<CategoryDTO> list();

    /**
     * 列表(文章个数)
     *
     * @return List
     */
    List<CategoryWithPostCountDTO> listCategoryWithPostCountDto();

    /**
     * 将所有分类列为树
     *
     * @return List
     */
    List<CategoryVO> listAsTree();

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
     * @param categoryId categoryId
     * @return boolean
     */
    boolean removeCategoryAndPostCategoryById(Integer categoryId);


}
