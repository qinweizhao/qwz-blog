package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.PostTag;
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
public interface PostTagMapper extends BaseMapper<PostTag> {


    /**
     * listByPostId
     * @param postIds postIds
     * @return List
     */
    default List<PostTag> listByPostId(Collection<Integer> postIds){
        return this.selectList(new LambdaQueryWrapperX<PostTag>()
                .in(PostTag::getPostId,postIds)
        );
    }

}
