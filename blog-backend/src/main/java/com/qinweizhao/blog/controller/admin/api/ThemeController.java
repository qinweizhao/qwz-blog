package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
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
@RequestMapping("/api/admin/theme")
public class ThemeController {

    private final ThemeService themeService;

    private final ConfigService configService;

    /**
     * 获取主题(about)
     *
     * @return ThemeProperty
     */
    @GetMapping
    public ThemeProperty getProperty() {
        return themeService.getThemeProperty();
    }

    /**
     * 主题配置（yaml)
     *
     * @return List
     */
    @GetMapping("configurations")
    public List<Group> listConfigurations() {
        return themeService.listConfig();
    }


    /**
     * 配置(db)
     *
     * @return Map
     */
    @GetMapping("/map")
    public Map<String, Object> map(@RequestParam(required = false) List<String> keys) {
        return configService.getMap(ConfigType.PORTAL, keys);
    }


    /**
     * 保存
     *
     * @param configs configs
     */
    @PostMapping("/map")
    @DisableOnCondition
    public void map(@RequestBody Map<String, Object> configs) {
        configService.save(ConfigType.PORTAL, configs);
    }


}
