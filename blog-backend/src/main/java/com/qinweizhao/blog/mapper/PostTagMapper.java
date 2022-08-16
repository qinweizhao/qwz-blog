package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.PostTag;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.projection.TagPostPostCountProjection;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
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
public interface PostTagMapper extends BaseMapper<PostTag> {


    /**
     * listByPostId
     *
     * @param postIds postIds
     * @return List
     */
    default List<PostTag> listByPostId(Collection<Integer> postIds) {
        return this.selectList(new LambdaQueryWrapperX<PostTag>()
                .in(PostTag::getPostId, postIds)
        );
    }

    /**
     * 标签附加文章个数
     *
     * @return List
     */
    List<TagPostPostCountProjection> selectPostCount();

    /**
     * 通过标签 id 删除关联
     *
     * @param tagId tagId
     * @return boolean
     */
    default int deleteByTagId(Integer tagId) {
        return this.delete(new LambdaQueryWrapper<PostTag>()
                .eq(PostTag::getTagId, tagId)
        );
    }

    /**
     * 通过 postId 查询 tagId 集合
     *
     * @param postId postId
     * @return Set
     */
    Set<Integer> selectTagIdsByPostId(Integer postId);

    /**
     * 查询 id 集合
     *
     * @param tagId  tagId
     * @param status status
     * @return Set
     */
    Set<Integer> selectSetPostIdByTagIdAndPostStatus(@Param("tagId") Integer tagId, @Param("status") PostStatus status);

    /**
     * 通过文章 id 删除关联
     *
     * @param postId postId
     * @return boolean
     */
    default int deleteByPostId(Integer postId) {
        return this.delete(new LambdaQueryWrapper<PostTag>()
                .eq(PostTag::getPostId, postId)
        );
    }

    /**
     * 批量删除关联
     *
     * @param postId       postId
     * @param removeTagIds removeTagIds
     * @return int
     */
    default int deleteBatchByPostIdAndTagIds(Integer postId, Collection<Integer> removeTagIds) {
        return this.delete(new LambdaQueryWrapperX<PostTag>()
                .eq(PostTag::getPostId, postId)
                .in(PostTag::getTagId, removeTagIds)
        );
    }
}
