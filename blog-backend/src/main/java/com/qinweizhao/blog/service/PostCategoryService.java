package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.PostCategory;
import com.qinweizhao.blog.model.enums.PostStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Post category service interface.
 *
 * @since 2019-03-19
 */
public interface PostCategoryService extends IService<PostCategory> {

    /**
     * 按 id 集合列出类别列表图
     *
     * @param postIds postIds
     * @return Map
     */
    Map<Integer, List<CategoryDTO>> listCategoryListMap(Collection<Integer> postIds);

    /**
     * 分类集合
     *
     * @param postId postId
     * @return List
     */
    List<CategoryDTO> listByPostId(Integer postId);

    /**
     * 列表
     *
     * @param categoryId categoryId
     * @param published  published
     * @return List
     */
    List<PostSimpleDTO> listPostByCategoryIdAndPostStatus(Integer categoryId, PostStatus published);

    /**
     * 列表
     *
     * @param categorySlug categorySlug
     * @param status       status
     * @return List
     */
    List<PostSimpleDTO> listPostByCategorySlugAndPostStatus(String categorySlug, PostStatus status);


}
