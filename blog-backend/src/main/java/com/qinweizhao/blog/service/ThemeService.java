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
     * 可以修改的文件类型
     */
    String[] CAN_EDIT_SUFFIX = {".ftl", ".css", ".js", ".yaml", ".yml", ".properties"};

    /**
     * 主题截图名称
     */
    String THEME_SCREENSHOTS_NAME = "screenshot";

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
     * 自定义工作表模板前缀
     */
    String CUSTOM_SHEET_PREFIX = "sheet_";

    /**
     * Custom post template prefix.
     */
    String CUSTOM_POST_PREFIX = "post_";

    /**
     * Key to access the zip file url which is in the http response
     */
    String ZIP_FILE_KEY = "zipball_url";

    /**
     * Key to access the tag name which is in the http response
     */
    String TAG_KEY = "tag_name";

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
