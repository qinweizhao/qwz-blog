package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Menu;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

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
     * 查找所有菜单 team
     *
     * @return List
     */
    List<String> selectListTeam();

    /**
     * 通过父菜单 id 查找菜单
     *
     * @param parentIdId parentIdId
     * @return List
     */
    default List<Menu> selectListByParentId(Integer parentIdId) {
        return this.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, parentIdId)
        );
    }

    /**
     * 列表通过分组
     *
     * @param team team
     * @return List
     */
    default List<Menu> selectListByTeam(String team) {
        return this.selectList(new LambdaQueryWrapperX<Menu>()
                .eq(Menu::getTeam, team)
        );
    }


}
