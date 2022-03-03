package com.qinweizhao.site.service.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.qinweizhao.site.exception.AlreadyExistsException;
import com.qinweizhao.site.model.dto.MenuDTO;
import com.qinweizhao.site.model.entity.Menu;
import com.qinweizhao.site.model.params.MenuParam;
import com.qinweizhao.site.model.vo.MenuTeamVO;
import com.qinweizhao.site.model.vo.MenuVO;
import com.qinweizhao.site.repository.MenuRepository;
import com.qinweizhao.site.service.MenuService;
import com.qinweizhao.site.service.base.AbstractCrudService;
import com.qinweizhao.site.utils.ServiceUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MenuService implementation class.
 *
 * @author ryanwang
 * @date 2019-03-14
 */
@Service
public class MenuServiceImpl extends AbstractCrudService<Menu, Integer> implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        super(menuRepository);
        this.menuRepository = menuRepository;
    }

    @Override
    public @NotNull List<MenuDTO> listDtos(@NotNull Sort sort) {
        Assert.notNull(sort, "Sort info must not be null");

        return convertTo(listAll(sort));
    }

    @Override
    public @NotNull List<MenuTeamVO> listTeamVos(@NotNull Sort sort) {
        Assert.notNull(sort, "Sort info must not be null");

        // List all menus
        List<MenuDTO> menus = listDtos(sort);

        // Get teams
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

    @Override
    public List<MenuDTO> listByTeam(@NotNull String team, Sort sort) {
        List<Menu> menus = menuRepository.findByTeam(team, sort);
        return menus.stream().map(menu -> (MenuDTO) new MenuDTO().convertFrom(menu)).collect(Collectors.toList());
    }

    @Override
    public List<MenuVO> listByTeamAsTree(@NotNull String team, Sort sort) {
        Assert.notNull(team, "Team must not be null");

        List<Menu> menus = menuRepository.findByTeam(team, sort);

        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }

        MenuVO topLevelMenu = createTopLevelMenu();

        concreteTree(topLevelMenu, menus);

        return topLevelMenu.getChildren();
    }

    @Override
    public @NotNull Menu createBy(@NotNull MenuParam menuParam) {
        Assert.notNull(menuParam, "Menu param must not be null");

        // Create an return
        return create(menuParam.convertTo());
    }

    @Override
    public List<MenuVO> listAsTree(@NotNull Sort sort) {
        Assert.notNull(sort, "Sort info must not be null");

        // List all menu
        List<Menu> menus = listAll(sort);

        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }

        // Create top menu
        MenuVO topLevelMenu = createTopLevelMenu();

        // Concrete the tree
        concreteTree(topLevelMenu, menus);

        return topLevelMenu.getChildren();
    }

    @Override
    public List<Menu> listByParentId(@NotNull Integer id) {
        Assert.notNull(id, "Menu parent id must not be null");

        return menuRepository.findByParentId(id);
    }

    @Override
    public List<String> listAllTeams() {
        return menuRepository.findAllTeams();
    }

    @Override
    public @NotNull Menu create(@NotNull Menu menu) {
        return super.create(menu);
    }

    @Override
    public @NotNull Menu update(@NotNull Menu menu) {
        return super.update(menu);
    }

    /**
     * Concrete menu tree.
     *
     * @param parentMenu parent menu vo must not be null
     * @param menus      a list of menu
     */
    private void concreteTree(MenuVO parentMenu, List<Menu> menus) {
        Assert.notNull(parentMenu, "Parent menu must not be null");

        if (CollectionUtils.isEmpty(menus)) {
            return;
        }

        // Create children container for removing after
        List<Menu> children = new LinkedList<>();

        menus.forEach(menu -> {
            if (parentMenu.getId().equals(menu.getParentId())) {
                // Save child menu
                children.add(menu);

                // Convert to child menu vo
                MenuVO child = new MenuVO().convertFrom(menu);

                // Init children if absent
                if (parentMenu.getChildren() == null) {
                    parentMenu.setChildren(new LinkedList<>());
                }
                parentMenu.getChildren().add(child);
            }
        });

        // Remove all child menus
        menus.removeAll(children);

        // Foreach children vos
        if (!CollectionUtils.isEmpty(parentMenu.getChildren())) {
            parentMenu.getChildren().forEach(childMenu -> concreteTree(childMenu, menus));
        }
    }

    /**
     * Creates a top level menu.
     *
     * @return top level menu with id 0
     */
    @NonNull
    private MenuVO createTopLevelMenu() {
        MenuVO topMenu = new MenuVO();
        // Set default value
        topMenu.setId(0);
        topMenu.setChildren(new LinkedList<>());
        topMenu.setParentId(-1);
        return topMenu;
    }

    private List<MenuDTO> convertTo(List<Menu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }

        return menus.stream()
                .map(menu -> (MenuDTO) new MenuDTO().convertFrom(menu))
                .collect(Collectors.toList());
    }

    @Deprecated
    private void nameMustNotExist(@NonNull Menu menu) {
        Assert.notNull(menu, "Menu must not be null");

        boolean exist = false;

        if (ServiceUtils.isEmptyId(menu.getId())) {
            // Create action
            exist = menuRepository.existsByName(menu.getName());
        } else {
            // Update action
            exist = menuRepository.existsByIdNotAndName(menu.getId(), menu.getName());
        }

        if (exist) {
            throw new AlreadyExistsException("菜单 " + menu.getName() + " 已存在");
        }
    }
}
