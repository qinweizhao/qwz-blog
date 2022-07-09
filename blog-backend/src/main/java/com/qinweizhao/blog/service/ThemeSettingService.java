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
 * @date 2019-0I-08
 */
public interface ThemeSettingService extends IService<ThemeSetting> {


    /**
     * Saves theme setting.
     *
     * @param key     setting key must not be blank
     * @param value   setting value
     * @param themeId theme id must not be blank
     * @return theme setting or null if the key does not exist
     */

    @Transactional
    ThemeSetting save(String key, String value, String themeId);

    /**
     * Saves theme settings.
     *
     * @param settings theme setting map
     * @param themeId  theme id must not be blank
     */
    @Transactional
    void save(Map<String, Object> settings, String themeId);

    /**
     * Lists theme settings by theme id.
     *
     * @param themeId theme id must not be blank
     * @return a list of theme setting
     */

    List<ThemeSetting> listBy(String themeId);

    /**
     * Lists theme settings as map.
     *
     * @param themeId theme id must not be blank
     * @return theme setting map
     */

    Map<String, Object> listAsMapBy(String themeId);

    /**
     * Replace theme setting url in batch.
     *
     * @param oldUrl old blog url.
     * @param newUrl new blog url.
     * @return replaced theme settings.
     */
    List<ThemeSetting> replaceUrl(String oldUrl, String newUrl);

    /**
     * Delete unused theme setting.
     */
    void deleteInactivated();
}
