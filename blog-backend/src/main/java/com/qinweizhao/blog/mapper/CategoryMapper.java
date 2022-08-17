package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author qinweizhao
 * @since 2022/7/5
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 列表
     *
     * @return List
     */
    default List<Category> selectList() {
        return selectList(new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getPriority)
        );
    }

    /**
     * 按类别名称计数
     *
     * @param name name
     * @return the count
     */
    default long selectCountByName(String name) {
        return selectCount(new LambdaQueryWrapper<Category>().eq(Category::getName, name));
    }

    /**
     * 按类别 ID 计数
     *
     * @param id id
     * @return the count
     */
    default long selectCountById(Integer id) {
        return selectCount(new LambdaQueryWrapper<Category>().eq(Category::getId, id));
    }

    /**
     * 通过 Slug 获取分类
     *
     * @param slug slug
     * @return Category
     */
    default Category selectBySlug(String slug) {
        return selectOne(new LambdaQueryWrapper<Category>().eq(Category::getSlug, slug));
    }

    /**
     * 通过 parentId 获取分类集合
     *
     * @param parentId parentId
     * @return List
     */
    default List<Category> selectListByParentId(Integer parentId) {
        return selectList(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, parentId)
                .orderByAsc(Category::getPriority)
        );
    }

    /**
     * 分类集合
     *
     * @param categoryIds categoryIds
     * @return List
     */
    default List<Category> selectListByIds(Set<Integer> categoryIds) {
        if (ObjectUtils.isEmpty(categoryIds)) {
            return new ArrayList<>();
        }
        return this.selectList(new LambdaQueryWrapperX<Category>()
                .inIfPresent(Category::getId, categoryIds)
        );
    }
}
