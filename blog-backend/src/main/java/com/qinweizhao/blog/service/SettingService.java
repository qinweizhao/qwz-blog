package com.qinweizhao.blog.service;

import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigDTO;
import com.qinweizhao.blog.model.param.SettingParam;
import com.qinweizhao.blog.model.param.SettingQueryParam;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Option service interface.
 *
 * @since 2019-03-14
 */
public interface SettingService {

    int DEFAULT_POST_PAGE_SIZE = 10;

    /**
     * 渲染模板
     */
    String RENDER_TEMPLATE = "%s/%s";

    /**
     * 渲染模板和后缀
     */
    String RENDER_TEMPLATE_SUFFIX = "%s/%s.ftl";


    String OPTIONS_KEY = "options";


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


    /**
     * 判断指定主题下是否存在模板
     *
     * @param template template
     * @return boolean
     */
    boolean templateExists(String template);


    /**
     * 配置文件名
     */
    String[] SETTINGS_NAMES = {"settings.yaml", "settings.yml"};

    /**
     * 构建完整路径
     *
     * @param articleId articleId
     * @return String
     */
    String buildFullPath(Integer articleId);

    /**
     * @param configs configs
     */
    void save(Map<String, Object> configs);

    /**
     * @param keys keys-
     * @return Map
     */
    Map<String, Object> getMap(List<String> keys);

    /**
     * Lists options by key list.
     *
     * @param keys key list
     * @return a map of option
     */
    Map<String, Object> listOptions(@Nullable List<String> keys);


    Object get(String key);

    /**
     * Gets post page size.
     *
     * @return page size
     */
    int getPostPageSize();


    /**
     * Gets blog base url. (Without /)
     *
     * @return blog base url (If blog url isn't present, current machine IP address will be default)
     */
    String getBlogBaseUrl();

    /**
     * 分页列表
     *
     * @param settingQueryParam optionQuery
     * @return PageResult
     */
    PageResult<ConfigDTO> pageSimple(SettingQueryParam settingQueryParam);

    /**
     * 新增
     *
     * @param param param
     * @return boolean
     */
    boolean save(SettingParam param);

    /**
     * 更新
     *
     * @param param param
     * @return boolean
     */
    boolean updateById(SettingParam param);

    /**
     * 删除
     *
     * @param optionId optionId
     * @return boolean
     */
    boolean removeById(Integer optionId);


    /**
     * 获取配置
     *
     * @return Map
     */
    @Transactional(rollbackFor = Exception.class)
    Map<String, Object> getSettings();


    /**
     * 获取配置
     *
     * @return List
     */
    List<Group> listConfig();
}
