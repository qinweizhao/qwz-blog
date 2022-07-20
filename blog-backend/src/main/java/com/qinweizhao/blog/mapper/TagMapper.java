package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author qinweizhao
 * @since 2022/7/6
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 统计
     *
     * @param name name
     * @param slug slug
     * @return long
     */
    default long countByNameOrSlug(String name, String slug) {
        return selectCount(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getName, name).or()
                .eq(Tag::getSlug, slug));
    }

    /**
     * 通过 slug 获取标签
     *
     * @param slug slug
     * @return Tag
     */
    default Tag selectBySlug(String slug) {
        return selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getSlug, slug));
    }

    /**
     * 按名称获取标签
     *
     * @param name name
     * @return Tag
     */
    default Tag getByName(@NonNull String name) {
        return selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));
    }

    /**
     * 查询标签集合
     * @param tagIds tagIds
     * @return List
     */
    default List<Tag> selectListByIds(Set<Integer> tagIds){
        if (ObjectUtils.isEmpty(tagIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<Tag>()
                .inIfPresent(Tag::getId,tagIds)
        );
    }
}
