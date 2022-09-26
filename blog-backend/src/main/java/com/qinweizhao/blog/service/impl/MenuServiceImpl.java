package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.mapper.MenuMapper;
import com.qinweizhao.blog.model.convert.MenuConvert;
import com.qinweizhao.blog.model.dto.MenuDTO;
import com.qinweizhao.blog.model.entity.Menu;
import com.qinweizhao.blog.model.param.MenuParam;
import com.qinweizhao.blog.model.vo.MenuTeamVO;
import com.qinweizhao.blog.service.MenuService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * MenuService implementation class.
 *
 * @author qinweizhao
 * @since 2019-03-14
 */
@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    public List<MenuDTO> list() {
        List<Menu> list = menuMapper.selectList(null);
        return MenuConvert.INSTANCE.convertToDTO(list);
    }

    @Override
    public List<MenuDTO> listAsTree() {

        List<Menu> menus = menuMapper.selectList(null);

        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }

        MenuDTO topLevelMenu = createTopLevelMenu();

        concreteTree(topLevelMenu, menus);

        return topLevelMenu.getChildren();
    }

    @Override
    public MenuDTO getById(Integer menuId) {
        return MenuConvert.INSTANCE.convert(menuMapper.selectById(menuId));
    }

    @Override
    public boolean save(MenuParam menuParam) {
        Menu menu = MenuConvert.INSTANCE.convert(menuParam);
        return menuMapper.insert(menu) > 0;
    }

    @Override
    public boolean saveBatch(List<MenuParam> params) {
        List<Menu> list = MenuConvert.INSTANCE.convertToDO(params);
        list.forEach(menuMapper::insert);
        return true;
    }

    @Override
    public boolean removeById(Integer menuId) {
        List<Menu> menus = menuMapper.selectListByParentId(menuId);
        if (ObjectUtils.isEmpty(menus)) {
            menus.forEach(menu -> {
                menu.setParentId(0);
                menuMapper.updateById(menu);
            });
        }
        return menuMapper.deleteById(menuId) > 0;
    }

    @Override
    public boolean removeByIds(List<Integer> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return true;
        }
        int i = menuMapper.deleteBatchIds(menuIds);
        int size = menuIds.size();
        return size == i;
    }

    @Override
    public boolean updateById(Integer menuId, MenuParam param) {
        // 获取菜单
        Menu menu = MenuConvert.INSTANCE.convert(param);
        menu.setId(menuId);
        return menuMapper.updateById(menu) > 0;
    }

    @Override
    public List<String> listTeams() {
        return menuMapper.selectListTeam();
    }

    @Override
    public boolean updateBatchById(List<MenuParam> menuParams) {
        List<Menu> menus = MenuConvert.INSTANCE.convertToDO(menuParams);
        menus.forEach(menuMapper::updateById);
        return true;
    }

    @Override
    public List<MenuDTO> listByTeamAsTree(String team) {
        Assert.notNull(team, "Team must not be null");

        List<Menu> menus = menuMapper.selectListByTeam(team);

        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }

        MenuDTO topLevelMenu = createTopLevelMenu();

        concreteTree(topLevelMenu, menus);

        return topLevelMenu.getChildren();
    }

    @Override
    public List<MenuDTO> listByTeam(String team) {
        List<Menu> menus = menuMapper.selectListByTeam(team);
        return MenuConvert.INSTANCE.convertToDTO(menus);
    }

    @Override
    public long count() {
        return menuMapper.selectCount(Wrappers.emptyWrapper());
    }

    @Override
    public List<MenuTeamVO> listTeamVO() {

        List<MenuDTO> menus = this.list();

        Set<String> teams = ServiceUtils.fetchProperty(menus, MenuDTO::getTeam);

        // Convert to team menu list map (Key: team, value: menu list)
        Map<String, List<MenuDTO>> teamMenuListMap = ServiceUtils.convertToListMap(teams, menus, MenuDTO::getTeam);

        List<MenuTeamVO> result = new LinkedList<>();

        // Wrap menu team vo list
        teamMenuListMap.forEach((team, menuList) -> {
            // Build menu team vo
            MenuTeamVO menuTeamVO = new MenuTeamVO();
            menuTeamVO.setTeam(team);
            menuTeamVO.setMenus(menuList);

            // Add it to result
            result.add(menuTeamVO);
        });

        return result;
    }


    /**
     * 创建顶级菜单(id 为0)
     *
     * @return MenuVO
     */
    private MenuDTO createTopLevelMenu() {
        MenuDTO topMenu = new MenuDTO();

        topMenu.setId(0);
        topMenu.setChildren(new LinkedList<>());
        topMenu.setParentId(-1L);
        return topMenu;
    }


    /**
     * 具体的菜单树
     *
     * @param parentMenu parentMenu
     * @param menus      menus
     */
    private void concreteTree(MenuDTO parentMenu, List<Menu> menus) {
        Assert.notNull(parentMenu, "父菜单不能为空");

        if (CollectionUtils.isEmpty(menus)) {
            return;
        }

        // 创建子容器以在之后删除
        List<Menu> children = new LinkedList<>();

        menus.forEach(menu -> {
            if (parentMenu.getId().equals(menu.getParentId())) {

                children.add(menu);

                MenuDTO child = MenuConvert.INSTANCE.convert(menu);

                if (parentMenu.getChildren() == null) {
                    parentMenu.setChildren(new LinkedList<>());
                }
                parentMenu.getChildren().add(child);
            }
        });

        // 删除所有子菜单
        menus.removeAll(children);

        // 遍历子
        if (!CollectionUtils.isEmpty(parentMenu.getChildren())) {
            parentMenu.getChildren().forEach(childMenu -> concreteTree(childMenu, menus));
        }
    }

}
