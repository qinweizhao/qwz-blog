package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.dto.ArticleSimpleDTO;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.entity.ArticleCategory;
import com.qinweizhao.blog.model.enums.ArticleStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Post category service interface.
 *
 * @author qinweizhao
 * @since 2019-03-14
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {

    /**
     * 按 id 集合列出类别列表图
     *
     * @param articleIds articleIds
     * @return Map
     */
    Map<Integer, List<CategoryDTO>> listCategoryListMap(Collection<Integer> articleIds);

    /**
     * 分类集合
     *
     * @param articleId articleId
     * @return List
     */
    List<CategoryDTO> listByArticleId(Integer articleId);

    /**
     * 列表
     *
     * @param categoryId categoryId
     * @param published  published
     * @return List
     */
    List<ArticleSimpleDTO> listPostByCategoryIdAndPostStatus(Integer categoryId, ArticleStatus published);

    /**
     * 列表
     *
     * @param categorySlug categorySlug
     * @param status       status
     * @return List
     */
    List<ArticleSimpleDTO> listPostByCategorySlugAndPostStatus(String categorySlug, ArticleStatus status);


}
