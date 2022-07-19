package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.dto.MenuDTO;
import com.qinweizhao.blog.model.params.MenuParam;

import java.util.List;

/**
 * Menu service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-14
 */
public interface MenuService {

    /**
     * 列表
     *
     * @return List
     */
    List<MenuDTO> list();

    /**
     * 树
     *
     * @return List
     */
    List<MenuDTO> listAsTree();

    /**
     * 详情
     *
     * @param menuId menuId
     * @return MenuDTO
     */
    MenuDTO getById(Integer menuId);

    /**
     * 新增菜单
     *
     * @param menuParam menuParam
     * @return boolean
     */
    boolean save(MenuParam menuParam);

    /**
     * 批量保存
     *
     * @param params params
     * @return boolean
     */
    boolean saveBatch(List<MenuParam> params);

    /**
     * 删除菜单(包含子菜单)
     * @param menuId menuId
     * @return boolean
     */
    boolean removeById(Integer menuId);

    /**
     * 批量删除菜单
     *
     * @param menuIds menuIds
     * @return boolean
     */
    boolean removeByIds(List<Integer> menuIds);

    /**
     * 更新菜单
     * @param menuId
     * @param param
     * @return
     */
    boolean updateById(Integer menuId, MenuParam param);


//
//    /**
//     * Lists all menu dtos.
//     *
//     * @param sort must not be null
//     * @return a list of menu output dto
//     */
//    List<MenuDTO> list(Sort sort);
//
//    /**
//     * Lists menu team vos.
//     *
//     * @param sort must not be null
//     * @return a list of menu team vo
//     */
//    @NonNull
//    List<MenuTeamVO> listTeamVos(@NonNull Sort sort);
//
//    /**
//     * List menus by team.
//     *
//     * @param team team
//     * @param sort sort
//     * @return list of menus
//     */
//    List<MenuDTO> listByTeam(@NonNull String team, Sort sort);

//    /**
//     * List menus by team as tree.
//     *
//     * @param team team
//     * @param sort sort
//     * @return list of tree menus
//     */
//    List<MenuVO> listByTeamAsTree(@NonNull String team, Sort sort);
//
//    /**
//     * Creates a menu.
//     *
//     * @param menuParam must not be null
//     * @return created menu
//     */
//    @NonNull
//    Menu createBy(@NonNull MenuParam menuParam);
//
//    /**
//     * Lists as menu tree.
//     *
//     * @param sort sort info must not be null
//     * @return a menu tree
//     */
//    List<MenuVO> listAsTree(@NonNull Sort sort);
//
//    /**
//     * Lists menu by parent id.
//     *
//     * @param id id
//     * @return a list of menu
//     */
//    List<Menu> listByParentId(@NonNull Integer id);

//    /**
//     * List all menu teams.
//     *
//     * @return a list of teams.
//     */
//    List<String> listAllTeams();
//
//
//    /**
//     * 列表
//     *
//     * @param sort sort
//     * @return List
//     */
//    List<MenuDTO> listMenu(Sort sort);
}
