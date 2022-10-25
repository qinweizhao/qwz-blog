package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.ThemeProperty;
import com.qinweizhao.blog.model.enums.ConfigType;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.ThemeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 主题管理
 *
 * @author qinweizhao
 * @since 2022/7/31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/themes")
public class ThemeController {

    private final ThemeService themeService;

    private final ConfigService configService;

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
        return themeService.listConfig();
    }

    /**
     * 主题设置
     *
     * @return Map
     */
    @GetMapping("settings")
    public Map<String, Object> setting() {
        return configService.getSettings();
    }

    /**
     * 保存主题设置
     *
     * @param settings settings
     */
    @PostMapping("settings")
    public Boolean saveSettings(@RequestBody Map<String, Object> settings) {
        return configService.save(settings, ConfigType.PORTAL);
    }

}
