package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/7/5
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 列表
     * @return List
     */
    default List<Category> selectList(){
        return selectList(new LambdaQueryWrapper<Category>()
                .orderByDesc(Category::getCreateTime)
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
     * 通过名称获取类别
     *
     * @param name name
     * @return Category
     */
    default Category selectByName(String name) {
        return selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, name));
    }


    /**
     * 通过 parentId 获取分类集合
     *
     * @param parentId parentId
     * @return List
     */
    default List<Category> selectListByParentId(@NonNull Integer parentId) {
        return selectList(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, parentId));
    }

}
