//package com.qinweizhao.blog.controller.admin.api;
//
//import com.qinweizhao.blog.convert.MenuConvert;
//import com.qinweizhao.blog.model.dto.MenuDTO;
//import com.qinweizhao.blog.model.entity.Menu;
//import com.qinweizhao.blog.model.params.MenuParam;
//import com.qinweizhao.blog.service.MenuService;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.SortDefault;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.data.domain.Sort.Direction.ASC;
//import static org.springframework.data.domain.Sort.Direction.DESC;
//
///**
// * Menu controller.
// *
// * @author johnniang
// * @author ryanwang
// * @author qinweizhao
// * @date 2019-04-03
// */
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/admin/menus")
//public class MenuController {
//
//    private final MenuService menuService;
//
//
//    /**
//     * 列表
//     *
//     * @param sort sort
//     * @return List
//     */
//    @GetMapping
//    public List<MenuDTO> listAll(@SortDefault(sort = "team", direction = DESC) Sort sort) {
//        return menuService.listMenu(sort.and(Sort.by(ASC, "priority")));
//    }
//
//
//    /**
//     * 树
//     *
//     * @param sort sort
//     * @return List
//     */
//    @GetMapping("tree_view")
//    public List<Menu> listAsTree(@SortDefault(sort = "team", direction = DESC) Sort sort) {
//        List<Menu> list = menuService.list();
//        return list;
//    }
//
////    @GetMapping("team/tree_view")
////    public List<MenuVO> listDefaultsAsTreeByTeam(@SortDefault(sort = "priority", direction = ASC) Sort sort, @RequestParam(name = "team") String team) {
////        return menuService.listByTeamAsTree(team, sort);
////    }
//
//    @GetMapping("{menuId:\\d+}")
//    public MenuDTO getBy(@PathVariable("menuId") Integer menuId) {
//        return MenuConvert.INSTANCE.convert(menuService.getById(menuId));
//    }
//
//    @PostMapping
//    public MenuDTO createBy(@RequestBody @Valid MenuParam menuParam) {
//        Menu menu = MenuConvert.INSTANCE.convert(menuParam);
//        menuService.save(menu);
//        return new MenuDTO();
//    }
//
//    @PostMapping("/batch")
//    public List<MenuDTO> createBatchBy(@RequestBody @Valid List<MenuParam> menuParams) {
//        List<Menu> list = MenuConvert.INSTANCE.convertToDO(menuParams);
//        boolean b = menuService.saveBatch(list);
//        return new ArrayList<>();
//    }
//
//    @PutMapping("{menuId:\\d+}")
//    public MenuDTO updateBy(@PathVariable("menuId") Integer menuId, @RequestBody @Valid MenuParam menuParam) {
//        // 获取菜单
//        Menu menu = MenuConvert.INSTANCE.convert(menuParam);
//        // 更新菜单的更改属性
//        boolean b = menuService.updateById(menu);
//        // 更新数据库中的菜单
//        return new MenuDTO();
//    }
//
//    @PutMapping("/batch")
//    public List<MenuDTO> updateBatchBy(@RequestBody @Valid List<MenuParam> menuParams) {
//
//        List<Menu> list = MenuConvert.INSTANCE.convertToDO(menuParams);
//        boolean b = menuService.updateBatchById(list);
//        return new ArrayList<>();
//    }
//
//    @DeleteMapping("{menuId:\\d+}")
//    public MenuDTO deleteBy(@PathVariable("menuId") Integer menuId) {
////        List<Menu> menus = menuService.listByParentId(menuId);
////        if (null != menus && menus.size() > 0) {
////            menus.forEach(menu -> {
////                menu.setParentId(0);
////                menuService.update(menu);
////            });
////        }
//        boolean b = menuService.removeById(menuId);
//        return new MenuDTO();
//    }
//
//    @DeleteMapping("/batch")
//    public List<MenuDTO> deleteBatchBy(@RequestBody List<Integer> menuIds) {
//        boolean b = menuService.removeByIds(menuIds);
//        return new ArrayList<>();
//    }
//
//    @GetMapping("teams")
//    public List<String> teams() {
//        return menuService.listAllTeams();
//    }
//}
