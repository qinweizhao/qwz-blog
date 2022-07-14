package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.PostCategory;
import com.qinweizhao.blog.utils.LambdaQueryWrapperX;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
public interface PostCategoryMapper extends BaseMapper<PostCategory> {

    /**
     * 通过 postId 集合查询关联
     *
     * @param postIds postIds
     * @return List
     */
    default List<PostCategory> selectListByPostIds(Collection<Integer> postIds) {
        return this.selectList(new LambdaQueryWrapperX<PostCategory>()
                .in(PostCategory::getPostId, postIds)
        );
    }
}
