package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.ThemeProperty;
import com.qinweizhao.blog.service.ThemeService;
import com.qinweizhao.blog.service.ThemeSettingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Theme controller.
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/themes")
public class ThemeController {

    private final ThemeService themeService;

    private final ThemeSettingService themeSettingService;

    /**
     * 通过主题 id 获取主题属性
     *
     * @param themeId themeId
     * @return ThemeProperty
     */
    @GetMapping("{themeId}")
    public ThemeProperty get(@PathVariable("themeId") String themeId) {
        return themeService.getThemeOfNonNullBy();
    }


    /**
     * 获取激活主题
     *
     * @return ThemeProperty
     */
    @GetMapping("activation")
    public ThemeProperty getActivateTheme() {
        return themeService.getThemeOfNonNullBy();
    }

    /**
     * 主题配置
     *
     * @param themeId themeId
     * @return List
     */
    @GetMapping("{themeId}/configurations")
    public List<Group> fetchConfig(@PathVariable("themeId") String themeId) {
        return themeService.fetchConfig();
    }


    /**
     * 按主题 ID 列出主题设置
     *
     * @param themeId themeId
     * @return Map
     */
    @GetMapping("{themeId}/settings")
    public Map<String, Object> listSetting(@PathVariable("themeId") String themeId) {
        return themeSettingService.listMap(themeId);
    }

    /**
     * 保存主题设置
     *
     * @param settings settings
     */
    @PostMapping("activation/settings")
    public Boolean saveSettingsBy(@RequestBody Map<String, Object> settings) {
        return themeSettingService.save(settings);
    }
//
//    @PostMapping("{themeId}/settings")
//    @ApiOperation("Saves theme settings")
//    @CacheLock(prefix = "save_theme_setting_by_themeId")
//    public void saveSettingsBy(@PathVariable("themeId") String themeId,
//                               @RequestBody Map<String, Object> settings) {
//        themeSettingService.save(settings, themeId);
//    }
//
//    @DeleteMapping("{themeId}")
//    @ApiOperation("Deletes a theme")
//    @DisableOnCondition
//    public void deleteBy(@PathVariable("themeId") String themeId,
//                         @RequestParam(value = "deleteSettings", defaultValue = "false") Boolean deleteSettings) {
//        themeService.deleteTheme(themeId, deleteSettings);
//    }
//
//    @PostMapping("upload")
//    @ApiOperation("Uploads a theme")
//    public ThemeProperty uploadTheme(@RequestPart("file") MultipartFile file) {
//        return themeService.upload(file);
//    }
//
////    @PutMapping("upload/{themeId}")
////    @ApiOperation("Upgrades theme by file")
////    public ThemeProperty updateThemeByUpload(@PathVariable("themeId") String themeId,
////            @RequestPart("file") MultipartFile file) {
////        return themeService.update(themeId, file);
////    }
//
//    @PostMapping("fetching")
//    @ApiOperation("Fetches a new theme")
//    public ThemeProperty fetchTheme(@RequestParam("uri") String uri) {
//        return themeService.fetch(uri);
//    }
//
//    @PostMapping("fetchingBranches")
//    @ApiOperation("Fetches all branches")
//    public List<ThemeProperty> fetchBranches(@RequestParam("uri") String uri) {
//        return themeService.fetchBranches(uri);
//    }
//
//    @GetMapping("fetchBranch")
//    @ApiOperation("Fetch specific branch")
//    public ThemeProperty fetchBranch(@RequestParam("uri") String uri, @RequestParam("branch") String branchName) {
//        return themeService.fetchBranch(uri, branchName);
//    }
//
//    @PostMapping("reload")
//    @ApiOperation("Reloads themes")
//    public void reload() {
//        themeService.reload();
//    }
//
//    @GetMapping(value = "activation/template/exists")
//    @ApiOperation("Determines if template exists")
//    public BaseResponse<Boolean> exists(@RequestParam(value = "template") String template) {
//        return BaseResponse.ok(themeService.templateExists(template));
//    }
}
