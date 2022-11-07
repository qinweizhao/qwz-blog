package com.qinweizhao.blog.service;

import com.qiniu.storage.Region;
import com.qinweizhao.blog.exception.MissingPropertyException;
import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.ThemeProperty;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigDTO;
import com.qinweizhao.blog.model.enums.ConfigType;
import com.qinweizhao.blog.model.param.ConfigParam;
import com.qinweizhao.blog.model.param.ConfigQueryParam;
import com.qinweizhao.blog.model.properties.PropertyEnum;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Option service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-03-14
 */
public interface ConfigService {

    int DEFAULT_POST_PAGE_SIZE = 10;

    int DEFAULT_ARCHIVES_PAGE_SIZE = 10;

    int DEFAULT_COMMENT_PAGE_SIZE = 10;

    String OPTIONS_KEY = "options";




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
     * 构建完整路径
     *
     * @param postId postId
     * @return String
     */
    String buildFullPath(Integer postId);

    /**
     *
     *
     * @param type type
     * @param configs configs
     */
    void save(ConfigType type,Map<String, Object> configs);

    /**
     * Get all options
     *
     * @return Map
     */
    @Transactional
    Map<String, Object> getMap();


    /**
     *
     * @param type type-区分前后台配置
     * @param keys keys-
     * @return Map
     */
    Map<String, Object> getMap(ConfigType type,List<String> keys);

    /**
     * Lists options by key list.
     *
     * @param keys key list
     * @return a map of option
     */
    Map<String, Object> listOptions(@Nullable List<String> keys);


    /**
     * Gets option value of not null.
     *
     * @param key option key must not be null
     * @return option value of not null
     */

    Object getByKeyOfNonNull(String key);

    /**
     * Get option by key
     *
     * @param key option key must not be blank
     * @return an optional option value
     */

    Optional<Object> getByKey(String key);

    /**
     * Gets option value by blog property.
     *
     * @param property blog property
     * @return an optiona value
     * @throws MissingPropertyException throws when property value dismisses
     */

    Object getByPropertyOfNonNull(PropertyEnum property);

    /**
     * Gets option value by blog property.
     *
     * @param property blog property must not be null
     * @return an optional option value
     */

    Optional<Object> getByProperty(PropertyEnum property);

    /**
     * Gets property value by blog property.
     *
     * @param property     blog property must not be null
     * @param propertyType property type must not be null
     * @param defaultValue default value
     * @param <T>          property type
     * @return property value
     */
    <T> T getByPropertyOrDefault(PropertyEnum property, Class<T> propertyType, T defaultValue);

    /**
     * Gets property value by blog property.
     * <p>
     * Default value from property default value.
     *
     * @param property     blog property must not be null
     * @param propertyType property type must not be null
     * @param <T>          property type
     * @return property value
     */
    <T> T getByPropertyOrDefault(PropertyEnum property, Class<T> propertyType);

    /**
     * Gets property value by blog property.
     *
     * @param property     blog property must not be null
     * @param propertyType property type must not be null
     * @param <T>          property type
     * @return property value
     */
    <T> Optional<T> getByProperty(PropertyEnum property, Class<T> propertyType);


    /**
     * Gets value by key.
     *
     * @param key       key must not be null
     * @param valueType value type must not be null
     * @param <T>       value type
     * @return value
     */

    <T> Optional<T> getByKey(String key, Class<T> valueType);

    /**
     * Gets enum value by property.
     *
     * @param property  property must not be blank
     * @param valueType enum value type must not be null
     * @param <T>       enum value type
     * @return an optional enum value
     */

    <T extends Enum<T>> Optional<T> getEnumByProperty(PropertyEnum property, Class<T> valueType);

    /**
     * Gets enum value by property.
     *
     * @param property     property must not be blank
     * @param valueType    enum value type must not be null
     * @param defaultValue default value
     * @param <T>          enum value type
     * @return enum value
     */
    @Nullable
    <T extends Enum<T>> T getEnumByPropertyOrDefault(PropertyEnum property, Class<T> valueType, @Nullable T defaultValue);

    /**
     * Gets post page size.
     *
     * @return page size
     */
    int getPostPageSize();

    /**
     * Gets archives page size.
     *
     * @return page size
     */
    int getArchivesPageSize();

    /**
     * Gets comment page size.
     *
     * @return page size
     */
    int getCommentPageSize();

    /**
     * Get qiniu oss region.
     *
     * @return qiniu region
     */

    Region getQiniuRegion();

    /**
     * Gets locale.
     *
     * @return locale user set or default locale
     */

    Locale getLocale();

    /**
     * Gets blog base url. (Without /)
     *
     * @return blog base url (If blog url isn't present, current machine IP address will be default)
     */
    String getBlogBaseUrl();

    /**
     * Gets blog title.
     *
     * @return blog title.
     */

    String getBlogTitle();

    /**
     * Gets global seo keywords.
     *
     * @return keywords
     */
    String getSeoKeywords();

    /**
     * Get global seo description.
     *
     * @return description
     */
    String getSeoDescription();

    /**
     * 获取日志页面自定义前缀
     *
     * @return journals page prefix.
     */
    String getJournalsPrefix();


    /**
     * Get archives custom prefix.
     *
     * @return archives prefix.
     */
    String getArchivesPrefix();

    /**
     * Get categories custom prefix.
     *
     * @return categories prefix.
     */
    String getCategoriesPrefix();

    /**
     * Get tags custom prefix.
     *
     * @return tags prefix.
     */
    String getTagsPrefix();

    /**
     * Get tags custom prefix.
     *
     * @return tags prefix.
     */
    String getArticlePrefix();

    /**
     * 分页列表
     *
     * @param configQueryParam optionQuery
     * @return PageResult
     */
    PageResult<ConfigDTO> pageSimple(ConfigQueryParam configQueryParam);

    /**
     * 新增
     *
     * @param param param
     * @return boolean
     */
    boolean save(ConfigParam param);

    /**
     * 更新
     *
     * @param param param
     * @return boolean
     */
    boolean updateById(ConfigParam param);

    /**
     * 删除
     *
     * @param optionId optionId
     * @return boolean
     */
    boolean removeById(Integer optionId);



    /**
     * 列出主题设置（前台）
     *
     * @return Map
     */
    Map<String, Object> getSettings();


    /**
     * 获取主题属性
     *
     * @return ThemeProperty
     */
    ThemeProperty getThemeProperty();

    /**
     * 获取配置
     *
     * @return List
     */
    List<Group> listConfig();
}
