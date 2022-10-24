package com.qinweizhao.blog.service;

import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.ThemeProperty;

import java.util.List;
import java.util.Optional;

/**
 * Theme service interface.
 *
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-26
 */
public interface ThemeService {


    /**
     * 配置文件名
     */
    String[] SETTINGS_NAMES = {"settings.yaml", "settings.yml"};

    /**
     * 渲染模板
     */
    String RENDER_TEMPLATE = "%s/%s";

    /**
     * 渲染模板和后缀
     */
    String RENDER_TEMPLATE_SUFFIX = "%s/%s.ftl";

    /**
     * 主题缓存 key
     */
    String THEMES_CACHE_KEY = "themes";

    /**
     * 获取主题属性
     *
     * @return ThemeProperty
     */
    ThemeProperty getThemeProperty();

    /**
     * 判断指定主题下是否存在模板
     *
     * @param template template
     * @return boolean
     */
    boolean templateExists(String template);

    /**
     * 获取主题属性(Optional)
     *
     * @return activated theme property
     */
    Optional<ThemeProperty> fetchActivatedTheme();

    /**
     * 获取配置
     *
     * @return List
     */
    List<Group> listConfig();

    /**
     * 渲染页面
     *
     * @param pageName pageName
     * @return String
     */
    String render(String pageName);

    /**
     * 渲染指定页面
     *
     * @param pageName pageName
     * @return String
     */
    String renderWithSuffix(String pageName);

}
