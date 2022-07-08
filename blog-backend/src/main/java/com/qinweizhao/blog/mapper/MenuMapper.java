package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/7/5
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 查询菜单名是否已经存在
     *
     * @param name name
     * @return true or false
     */
    default boolean existsByName(String name) {
        return exists(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getName, name));
    }

    /**
     * 通过 id 和 name 查询菜单名称是否已经存在
     *
     * @param id   id
     * @param name name
     * @return boolean
     */
    default boolean existsByIdNotAndName(Integer id, String name) {
        return exists(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getId, id)
                .eq(Menu::getName, name));
    }

    /**
     * Finds by menu parent id.
     *
     * @param parentId parentId
     * @return List
     */
    default List<Menu> selectByParentId(Integer parentId) {
        return selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, parentId));
    }

    /**
     * 由菜单 team 查找。
     *
     * @param team team
     * @param sort sort
     * @return List
     */
    default List<Menu> selectByTeam(@NonNull String team, Sort sort) {
        return selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getTeam, team)
        );
    }

    /**
     * 查找所有菜单 team
     *
     * @return List
     */
    List<String> selectListTeam();
}
