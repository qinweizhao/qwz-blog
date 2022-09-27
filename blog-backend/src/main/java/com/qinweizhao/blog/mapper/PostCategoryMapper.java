package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.PostCategory;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.projection.CategoryPostCountProjection;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Mapper
public interface PostCategoryMapper extends BaseMapper<PostCategory> {

    /**
     * 通过 postId 集合查询关联
     *
     * @param postIds postIds
     * @return List
     */
    default List<PostCategory> selectListByPostIds(Collection<Integer> postIds) {
        return this.selectList(new LambdaQueryWrapper<PostCategory>().in(PostCategory::getPostId, postIds));
    }

    /**
     * 每个分类包含多少文章数量
     *
     * @return List
     */
    List<CategoryPostCountProjection> selectPostCount();


    /**
     * 通过分类 Id 删除关联
     *
     * @param categoryId categoryId
     * @return int
     */
    default int deleteByCategoryId(Integer categoryId) {
        return this.delete(new LambdaQueryWrapper<PostCategory>().eq(PostCategory::getCategoryId, categoryId));
    }

    /**
     * 查询分类 id 集合
     *
     * @param postId postId
     * @return Set
     */
    Set<Integer> selectSetCategoryIdsByPostId(Integer postId);


    /**
     * 查询 id 集合
     *
     * @param categoryId categoryId
     * @param status     status
     * @return Set
     */
    Set<Integer> selectSetPostIdByCategoryIdAndPostStatus(@Param("categoryId") Integer categoryId, @Param("status") PostStatus status);

    /**
     * 通过文章 Id 删除关联
     *
     * @param postId postId
     * @return int
     */
    default int deleteByPostId(Integer postId) {
        return this.delete(new LambdaQueryWrapper<PostCategory>().eq(PostCategory::getPostId, postId));
    }

    /**
     * 批量删除关联
     *
     * @param postId            postId
     * @param removeCategoryIds removeCategoryIds
     * @return int
     */
    default int deleteBatchByPostIdAndTagIds(Integer postId, Collection<Integer> removeCategoryIds) {
        return this.delete(new LambdaQueryWrapperX<PostCategory>().eq(PostCategory::getPostId, postId).in(PostCategory::getCategoryId, removeCategoryIds));
    }
}
