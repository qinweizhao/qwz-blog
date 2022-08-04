package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.dto.CategoryWithPostCountDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.entity.PostCategory;
import com.qinweizhao.blog.model.enums.PostStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Post category service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-19
 */
public interface PostCategoryService extends IService<PostCategory> {

    /**
     * 按 id 集合列出类别列表图
     * @param postIds postIds
     * @return Map
     */
    Map<Integer, List<Category>> listCategoryListMap(Collection<Integer> postIds);

    /**
     * 分类集合
     * @param postId postId
     * @return List
     */
    List<CategoryDTO> listCategoriesByPostId(Integer postId);

//    /**
//     * 列表
//     * @param categoryId categoryId
//     * @param published published
//     * @return List
//     */
//    List<PostSimpleDTO> listPostByCategoryIdAndPostStatus(Integer categoryId, PostStatus published);
//
//    /**
//     * 列表
//     * @param categorySlug categorySlug
//     * @param status status
//     * @return List
//     */
//    List<PostSimpleDTO> listPostByCategorySlugAndPostStatus(String categorySlug, PostStatus status);

    /**
     * 列表(文章个数)
     *
     * @return List
     */
    List<CategoryWithPostCountDTO> listCategoryWithPostCountDto();


//
//    /**
//     * Lists post by category id.
//     *
//     * @param categoryId category id must not be null
//     * @return a list of post
//     */
//    @NonNull
//    List<Post> listPostBy(@NonNull Integer categoryId);
//
//    /**
//     * Lists post by category id and post status.
//     *
//     * @param categoryId category id must not be null
//     * @param status     post status
//     * @return a list of post
//     */
//    @NonNull
//    List<Post> listPostBy(@NonNull Integer categoryId, @NonNull PostStatus status);
//
//    /**
//     * Lists post by category slug and post status.
//     *
//     * @param slug   category slug must not be null
//     * @param status post status
//     * @return a list of post
//     */
//    @NonNull
//    List<Post> listPostBy(@NonNull String slug, @NonNull PostStatus status);
//
//    /**
//     * Pages post by category id.
//     *
//     * @param categoryId category id must not be null
//     * @param pageable   pageable
//     * @return page of post
//     */
//    @NonNull
//    Page<Post> pagePostBy(@NonNull Integer categoryId, Pageable pageable);
//
//    /**
//     * Pages post by category id and post status.
//     *
//     * @param categoryId category id must not be null
//     * @param status     post status
//     * @param pageable   pageable
//     * @return page of post
//     */
//    @NonNull
//    Page<Post> pagePostBy(@NonNull Integer categoryId, @NonNull PostStatus status, Pageable pageable);
//
//    /**
//     * Merges or creates post categories by post id and category id set if absent.
//     *
//     * @param postId      post id must not be null
//     * @param categoryIds category id set
//     * @return a list of post category
//     */
//    @NonNull
//    List<PostCategory> mergeOrCreateByIfAbsent(@NonNull Integer postId, @Nullable Set<Integer> categoryIds);
//
//    /**
//     * Lists by post id.
//     *
//     * @param postId post id must not be null
//     * @return a list of post category
//     */
//    @NonNull
//    List<PostCategory> listByPostId(@NonNull Integer postId);
//
//    /**
//     * Lists by category id.
//     *
//     * @param categoryId category id must not be null
//     * @return a list of post category
//     */
//    @NonNull
//    List<PostCategory> listByCategoryId(@NonNull Integer categoryId);
//
//    /**
//     * List category id set by post id.
//     *
//     * @param postId post id must not be null
//     * @return a set of category id
//     */
//    @NonNull
//    Set<Integer> listCategoryIdsByPostId(@NonNull Integer postId);
//
//    /**
//     * Removes post categories by post id.
//     *
//     * @param postId post id must not be null
//     * @return a list of post category deleted
//     */
//    @NonNull
//    @Transactional
//    List<PostCategory> removeByPostId(@NonNull Integer postId);
//
//    /**
//     * Removes post categories by category id.
//     *
//     * @param categoryId category id must not be null
//     * @return a list of post category deleted
//     */
//    @NonNull
//    @Transactional
//    List<PostCategory> removeByCategoryId(@NonNull Integer categoryId);
//
//    /**
//     * Lists category with post count.
//     *
//     * @param sort sort info
//     * @return a list of category dto
//     */
//    @NonNull
//    List<CategoryWithPostCountDTO> listCategoryWithPostCountDto(@NonNull Sort sort);
}
