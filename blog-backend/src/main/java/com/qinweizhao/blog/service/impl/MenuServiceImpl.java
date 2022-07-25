package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.convert.MenuConvert;
import com.qinweizhao.blog.mapper.MenuMapper;
import com.qinweizhao.blog.model.dto.MenuDTO;
import com.qinweizhao.blog.model.entity.Menu;
import com.qinweizhao.blog.model.params.MenuParam;
import com.qinweizhao.blog.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * MenuService implementation class.
 *
 * @author qinweizhao
 * @date 2019-03-14
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

        // Concrete the tree
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
        if (null != menus && menus.size() > 0) {
            menus.forEach(menu -> {
                menu.setParentId(0);
                menuMapper.updateById(menu);
            });
        }
        return true;
    }

    @Override
    public boolean removeByIds(List<Integer> menuIds) {
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
                // Save child menu
                children.add(menu);

                // Convert to child menu vo
                MenuDTO child = MenuConvert.INSTANCE.convert(menu);
                // Init children if absent
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
