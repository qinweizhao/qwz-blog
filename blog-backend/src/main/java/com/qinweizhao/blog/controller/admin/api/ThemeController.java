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
     * 获取主题属性
     *
     * @return ThemeProperty
     */
    @GetMapping
    public ThemeProperty getProperty() {
        return themeService.getThemeProperty();
    }


    /**
     * 主题配置
     *
     * @return List
     */
    @GetMapping("configurations")
    public List<Group> listConfigurations() {
        return themeService.fetchConfig();
    }


    /**
     * 主题设置
     *
     * @return Map
     */
    @GetMapping("settings")
    public Map<String, Object> setting() {
        return themeSettingService.getSettings();
    }

    /**
     * 保存主题设置
     *
     * @param settings settings
     */
    @PostMapping("settings")
    public Boolean saveSettings(@RequestBody Map<String, Object> settings) {
        return themeSettingService.save(settings);
    }

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
