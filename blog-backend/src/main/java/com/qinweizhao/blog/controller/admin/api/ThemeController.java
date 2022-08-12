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

}
