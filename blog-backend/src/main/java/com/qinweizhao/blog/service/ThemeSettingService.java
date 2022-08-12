package com.qinweizhao.blog.service;

import java.util.Map;

/**
 * Theme setting service interface.
 *
 * @author qinweizhao
 * @since 2019-0I-08
 */
public interface ThemeSettingService {

    /**
     * 列出主题设置
     *
     * @return Map
     */
    Map<String, Object> getSettings();

    /**
     * 保存主题配置
     *
     * @param settings settings
     * @return boolean
     */
    boolean save(Map<String, Object> settings);


}
