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
 * @date 2019-03-26
 */
public interface ThemeService {

    /**
     * 主题属性文件名
     */
    String THEME_PROPERTY_FILE_NAME = "theme.yaml";

    /**
     * 主题属性文件名
     */
    String[] THEME_PROPERTY_FILE_NAMES = {"theme.yaml", "theme.yml"};


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
     * Theme provider remote name.
     */
    String THEME_PROVIDER_REMOTE_NAME = "origin";

    /**
     * Default remote branch name.
     */
    String DEFAULT_REMOTE_BRANCH = "master";

    /**
     * Key to access the zip file url which is in the http response
     */
    String ZIP_FILE_KEY = "zipball_url";

    /**
     * Key to access the tag name which is in the http response
     */
    String TAG_KEY = "tag_name";

    /**
     * 通过主题 id 获取主题属性
     *
     * @return ThemeProperty
     */
    ThemeProperty getThemeOfNonNullBy( );


//    /**
//     * Get theme property by theme id.
//     *
//     * @param themeId theme id
//     * @return a optional theme property
//     */
//    @NonNull
//    Optional<ThemeProperty> fetchThemePropertyBy(@Nullable String themeId);
//
//    /**
//     * Gets all themes
//     *
//     * @return set of themes
//     */
//    @NonNull
//    List<ThemeProperty> getThemes();
//
//    /**
//     * Lists theme folder by theme name.
//     *
//     * @param themeId theme id
//     * @return List<ThemeFile>
//     */
//    @NonNull
//    List<ThemeFile> listThemeFolderBy(@NonNull String themeId);
//
//    /**
//     * Lists a set of custom template, such as sheet_xxx.ftl, and xxx will be template name
//     *
//     * @param themeId theme id must not be blank
//     * @return a set of templates
//     */
//    @Deprecated
//    @NonNull
//    List<String> listCustomTemplates(@NonNull String themeId);
//
//    /**
//     * Lists a set of custom template, such as sheet_xxx.ftl/post_xxx.ftl, and xxx will be template name
//     *
//     * @param themeId theme id must not be blank
//     * @param prefix  post_ or sheet_
//     * @return a set of templates
//     */
//    @NonNull
//    List<String> listCustomTemplates(@NonNull String themeId, @NonNull String prefix);
//

    /**
     * 判断指定主题下是否存在模板
     *
     * @param template template
     * @return boolean
     */
    boolean templateExists(String template);
//
//    /**
//     * Checks whether theme exists under template path
//     *
//     * @param themeId theme id
//     * @return boolean
//     */
//    boolean themeExists(@Nullable String themeId);
//
//    /**
//     * Gets theme base path.
//     *
//     * @return theme base path
//     */
//    Path getBasePath();
//
//    /**
//     * Gets template content by template absolute path.
//     *
//     * @param absolutePath absolute path
//     * @return template content
//     */
//    String getTemplateContent(@NonNull String absolutePath);
//
//    /**
//     * Gets template content by template absolute path and themeId.
//     *
//     * @param themeId      themeId
//     * @param absolutePath absolute path
//     * @return template content
//     */
//    String getTemplateContent(@NonNull String themeId, @NonNull String absolutePath);
//
//    /**
//     * Saves template content by template absolute path.
//     *
//     * @param absolutePath absolute path
//     * @param content      new content
//     */
//    void saveTemplateContent(@NonNull String absolutePath, @NonNull String content);
//
//    /**
//     * Saves template content by template absolute path and themeId.
//     *
//     * @param themeId      themeId
//     * @param absolutePath absolute path
//     * @param content      new content
//     */
//    void saveTemplateContent(@NonNull String themeId, @NonNull String absolutePath, @NonNull String content);
//
//    /**
//     * Deletes a theme by key.
//     *
//     * @param themeId        theme id must not be blank
//     * @param deleteSettings whether all settings of the specified theme should be deleted.
//     */
//    void deleteTheme(@NonNull String themeId, @NonNull Boolean deleteSettings);
//
//    /**
//     * Fetches theme configuration.
//     *
//     * @param themeId must not be blank
//     * @return theme configuration
//     */
//    @NonNull
//    List<Group> fetchConfig(@NonNull String themeId);
//
//    /**
//     * Renders a theme page.
//     *
//     * @param pageName must not be blank
//     * @return full path of the theme page
//     */
//    @NonNull
//    String render(@NonNull String pageName);
//
//    /**
//     * Renders a theme page.
//     *
//     * @param pageName must not be blank
//     * @return full path of the theme page
//     */
//    @NonNull
//    String renderWithSuffix(@NonNull String pageName);
//

    /**
     * 获取当前主题 ID
     *
     * @return String
     */
    String getActivatedThemeId();
//
//    /**
//     * Gets activated theme property.
//     *
//     * @return activated theme property
//     */
//    @NonNull
//    ThemeProperty getActivatedTheme();
//

    /**
     * Fetch activated theme property.
     *
     * @return activated theme property
     */
    Optional<ThemeProperty> fetchActivatedTheme();

    /**
     * 获取配置
     *
     * @return List
     */
    List<Group> fetchConfig();


    /**
     * render
     *
     * @param pageName pageName
     * @return String
     */
    String render(String pageName);

    /**
     * 渲染指定页面
     * @param pageName pageName
     * @return String
     */
    String renderWithSuffix(String pageName);

//
//    /**
//     * Actives a theme.
//     *
//     * @param themeId theme id must not be blank
//     * @return theme property
//     */
//    @NonNull
//    ThemeProperty activateTheme(@NonNull String themeId);
//
//    /**
//     * Upload theme.
//     *
//     * @param file multipart file must not be null
//     * @return theme info
//     */
//    @NonNull
//    ThemeProperty upload(@NonNull MultipartFile file);
//
//    /**
//     * Adds a new theme.
//     *
//     * @param themeTmpPath theme temporary path must not be null
//     * @return theme property
//     * @throws IOException IOException
//     */
//    @NonNull
//    ThemeProperty add(@NonNull Path themeTmpPath) throws IOException;
//
//    /**
//     * Fetches a new theme.
//     *
//     * @param uri theme remote uri must not be null
//     * @return theme property
//     */
//    @NonNull
//    ThemeProperty fetch(@NonNull String uri);
//
//    /**
//     * Fetches all the branches info
//     *
//     * @param uri theme remote uri must not be null
//     * @return list of theme properties
//     */
//    @NonNull
//    List<ThemeProperty> fetchBranches(@NonNull String uri);
//
//    /**
//     * Fetches a specific branch (clone)
//     *
//     * @param uri        theme remote uri must not be null
//     * @param branchName wanted branch must not be null
//     * @return theme property
//     */
//    @NonNull
//    ThemeProperty fetchBranch(@NonNull String uri, @NonNull String branchName);
//
//    /**
//     * Reloads themes
//     */
//    void reload();

}
