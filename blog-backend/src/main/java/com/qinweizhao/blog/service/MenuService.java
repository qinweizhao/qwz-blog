package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.dto.MenuDTO;
import com.qinweizhao.blog.model.params.MenuParam;
import com.qinweizhao.blog.model.vo.MenuTeamVO;

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
     *
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
     *
     * @param menuId menuId
     * @param param  param
     * @return boolean
     */
    boolean updateById(Integer menuId, MenuParam param);

    /**
     * 所有分组
     *
     * @return List
     */
    List<String> listTeams();

    /**
     * 批量更新
     *
     * @param menuParams menuParams
     * @return boolean¬
     */
    boolean updateBatchById(List<MenuParam> menuParams);

    /**
     * 分组树
     *
     * @param team team
     * @return List
     */
    List<MenuDTO> listByTeamAsTree(String team);


    /**
     * 列表按照分组
     *
     * @param team team
     * @return list of menus
     */
    List<MenuDTO> listByTeam(String team);

    /**
     * 个数
     * @return long
     */
    long count();


    /**
     * 分组+菜单 列表
     * @return List
     */
    List<MenuTeamVO> listTeamVO();



}
