package com.qinweizhao.blog.controller.admin;

import com.qinweizhao.blog.model.dto.MenuDTO;
import com.qinweizhao.blog.model.params.MenuParam;
import com.qinweizhao.blog.model.vo.MenuVO;
import com.qinweizhao.blog.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Menu controller.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-04-03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/menus")
public class MenuController {

    private final MenuService menuService;


    /**
     * 列表
     *
     * @return List
     */
    @GetMapping
    public List<MenuDTO> list() {
        return menuService.list();
    }


    /**
     * 树
     *
     * @return List
     */
    @GetMapping("tree_view")
    public List<MenuVO> listAsTree() {
        return menuService.listAsTree();
    }

//    @GetMapping("team/tree_view")
//    public List<MenuVO> listDefaultsAsTreeByTeam(@SortDefault(sort = "priority", direction = ASC) Sort sort, @RequestParam(name = "team") String team) {
//        return menuService.listByTeamAsTree(team, sort);
//    }
//

    /**
     * 详情
     *
     * @param menuId menuId
     * @return MenuDTO
     */
    @GetMapping("{menuId:\\d+}")
    public MenuDTO getBy(@PathVariable("menuId") Integer menuId) {
        return menuService.getById(menuId);
    }


    /**
     * 新增
     *
     * @param menuParam menuParam
     * @return MenuDTO
     */
    @PostMapping
    public Boolean createBy(@RequestBody @Valid MenuParam menuParam) {
        return menuService.save(menuParam);
    }

    /**
     * 批量新增
     *
     * @param params params
     * @return Boolean
     */
    @PostMapping("/batch")
    public Boolean createBatchBy(@RequestBody @Valid List<MenuParam> params) {
        return menuService.saveBatch(params);
    }

    /**
     * 更新菜单
     *
     * @param menuId menuId
     * @param param  param
     * @return Boolean
     */
    @PutMapping("{menuId:\\d+}")
    public Boolean updateBy(@PathVariable("menuId") Integer menuId, @RequestBody @Valid MenuParam param) {
        return menuService.updateById(menuId, param);
    }
//
//    @PutMapping("/batch")
//    public List<MenuDTO> updateBatchBy(@RequestBody @Valid List<MenuParam> menuParams) {
//
//        List<Menu> list = MenuConvert.INSTANCE.convertToDO(menuParams);
//        boolean b = menuService.updateBatchById(list);
//        return new ArrayList<>();
//    }
//

    /**
     * 删除菜单
     *
     * @param menuId menuId
     * @return Boolean
     */
    @DeleteMapping("{menuId:\\d+}")
    public Boolean deleteBy(@PathVariable("menuId") Integer menuId) {
        return menuService.removeById(menuId);

    }

    /**
     * 批量删除
     *
     * @param menuIds menuIds
     */
    @DeleteMapping("/batch")
    public Boolean deleteBatchBy(@RequestBody List<Integer> menuIds) {
        return menuService.removeByIds(menuIds);
    }
//
//    @GetMapping("teams")
//    public List<String> teams() {
//        return menuService.listAllTeams();
//    }
}
