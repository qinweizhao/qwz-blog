package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Menu;
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

//    /**
//     * 由菜单 team 查找。
//     *
//     * @param team team
//     * @param sort sort
//     * @return List
//     */
//    default List<Menu> selectByTeam(@NonNull String team, Sort sort) {
//        return selectList(new LambdaQueryWrapper<Menu>()
//                .eq(Menu::getTeam, team)
//        );
//    }

    /**
     * 查找所有菜单 team
     *
     * @return List
     */
    List<String> selectListTeam();
//
//    /**
//     * 查询列表
//     *
//     * @param sort sort
//     * @return List
//     */
//    default List<Menu> selectListMenu(Sort sort) {
//        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
//        Stream<Sort.Order> orderStream = sort.get();
//        orderStream.peek(item -> {
//            queryWrapper.orderBy(sort.isSorted(), item.isAscending(), item.getProperty());
//        });
//        return selectList(queryWrapper);
//    }

}
