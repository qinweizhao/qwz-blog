package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.entity.ThemeSetting;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Theme setting service interface.
 *
 * @author johnniang
 * @since 2019-0I-08
 */
public interface ThemeSettingService {

    /**
     * 列出主题设置
     * @return Map
     */
    Map<String, Object> getSettings();

    /**
     * 保存主题配置
     * @param settings  settings
     * @return boolean
     */
    boolean save(Map<String, Object> settings);

//
//    /**
//     * Saves theme setting.
//     *
//     * @param key     setting key must not be blank
//     * @param value   setting value
//     * @param themeId theme id must not be blank
//     * @return theme setting or null if the key does not exist
//     */
//
//    @Transactional
//    ThemeSetting save(String key, String value, String themeId);
//
//    /**
//     * Saves theme settings.
//     *
//     * @param settings theme setting map
//     * @param themeId  theme id must not be blank
//     */
//    @Transactional
//    void save(Map<String, Object> settings, String themeId);
//
//    /**
//     * Lists theme settings by theme id.
//     *
//     * @param themeId theme id must not be blank
//     * @return a list of theme setting
//     */
//
//    List<ThemeSetting> listBy(String themeId);
//
    /**
     * Lists theme settings as map.
     *
     * @return theme setting map
     */
    Map<String, Object> listAsMapBy();
//
//    /**
//     * Replace theme setting url in batch.
//     *
//     * @param oldUrl old blog url.
//     * @param newUrl new blog url.
//     * @return replaced theme settings.
//     */
//    List<ThemeSetting> replaceUrl(String oldUrl, String newUrl);
//
//    /**
//     * Delete unused theme setting.
//     */
//    void deleteInactivated();
}
