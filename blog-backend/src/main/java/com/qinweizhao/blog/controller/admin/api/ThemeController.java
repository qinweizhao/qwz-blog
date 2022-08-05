package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.ThemeProperty;
import com.qinweizhao.blog.service.ThemeService;
import com.qinweizhao.blog.service.ThemeSettingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return themeService.getThemeOfNonNullBy(themeId);
    }

//    @GetMapping
//    @ApiOperation("Lists all themes")
//    public List<ThemeProperty> listAll() {
//        return themeService.getThemes();
//    }
//
//    @GetMapping("activation/files")
//    @ApiOperation("Lists all activate theme files")
//    public List<ThemeFile> listFiles() {
//        return themeService.listThemeFolderBy(themeService.getActivatedThemeId());
//    }
//
//    @GetMapping("{themeId}/files")
//    @ApiOperation("Lists theme files by theme id")
//    public List<ThemeFile> listFiles(@PathVariable("themeId") String themeId) {
//        return themeService.listThemeFolderBy(themeId);
//    }
//
//    @GetMapping("files/content")
//    @ApiOperation("Gets template content")
//    public BaseResponse<String> getContentBy(@RequestParam(name = "path") String path) {
//        return BaseResponse.ok(HttpStatus.OK.getReasonPhrase(), themeService.getTemplateContent(path));
//    }
//
//    @GetMapping("{themeId}/files/content")
//    @ApiOperation("Gets template content by theme id")
//    public BaseResponse<String> getContentBy(@PathVariable("themeId") String themeId,
//                                             @RequestParam(name = "path") String path) {
//        return BaseResponse.ok(HttpStatus.OK.getReasonPhrase(), themeService.getTemplateContent(themeId, path));
//    }
//
//    @PutMapping("files/content")
//    @ApiOperation("Updates template content")
//    @DisableOnCondition
//    public void updateContentBy(@RequestBody ThemeContentParam param) {
//        themeService.saveTemplateContent(param.getPath(), param.getContent());
//    }
//
//    @PutMapping("{themeId}/files/content")
//    @ApiOperation("Updates template content by theme id")
//    @DisableOnCondition
//    public void updateContentBy(@PathVariable("themeId") String themeId,
//                                @RequestBody ThemeContentParam param) {
//        themeService.saveTemplateContent(themeId, param.getPath(), param.getContent());
//    }
//
//    @GetMapping("activation/template/custom/sheet")
//    @ApiOperation("Gets custom sheet templates")
//    public List<String> customSheetTemplate() {
//        return themeService.listCustomTemplates(themeService.getActivatedThemeId(), ThemeService.CUSTOM_SHEET_PREFIX);
//    }
//

//    @PostMapping("{themeId}/activation")
//    @ApiOperation("Activates a theme")
//    public ThemeProperty active(@PathVariable("themeId") String themeId) {
//        return themeService.activateTheme(themeId);
//    }


    /**
     * 获取激活主题
     *
     * @return ThemeProperty
     */
    @GetMapping("activation")
    public ThemeProperty getActivateTheme() {
        return themeService.getThemeOfNonNullBy(themeService.getActivatedThemeId());
    }
//
//    @GetMapping("activation/configurations")
//    @ApiOperation("Fetches activated theme configuration")
//    public BaseResponse<Object> fetchConfig() {
//        return BaseResponse.ok(themeService.fetchConfig(themeService.getActivatedThemeId()));
//    }
//

    /**
     * 主题配置
     *
     * @param themeId themeId
     * @return List
     */
    @GetMapping("{themeId}/configurations")
    public List<Group> fetchConfig(@PathVariable("themeId") String themeId) {
        return themeService.fetchConfig(themeId);
    }
//
//    @GetMapping("activation/settings")
//    @ApiOperation("Lists activated theme settings")
//    public Map<String, Object> listSettingsBy() {
//        return themeSettingService.listAsMapBy(themeService.getActivatedThemeId());
//    }
//

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
//
//    @PostMapping("activation/settings")
//    @ApiOperation("Saves theme settings")
//    public void saveSettingsBy(@RequestBody Map<String, Object> settings) {
//        themeSettingService.save(settings, themeService.getActivatedThemeId());
//    }
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
