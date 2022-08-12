package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.Item;
import com.qinweizhao.blog.mapper.ThemeSettingMapper;
import com.qinweizhao.blog.model.entity.ThemeSetting;
import com.qinweizhao.blog.service.ThemeService;
import com.qinweizhao.blog.service.ThemeSettingService;
import com.qinweizhao.blog.util.ServiceUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Theme setting service implementation.
 *
 * @author johnniang
 * @since 2019-04-08
 */
@Slf4j
@Service
@AllArgsConstructor
public class ThemeSettingServiceImpl implements ThemeSettingService {

    private final ThemeSettingMapper themeSettingMapper;

    private final ThemeService themeService;

    private final Configuration configuration;

    private final MyBlogProperties blogProperties;

    @Override
    public Map<String, Object> getSettings() {

        Map<String, Item> itemMap = getConfigItemMap();

        // Get theme setting
        List<ThemeSetting> themeSettings = themeSettingMapper.selectList(Wrappers.emptyWrapper());

        Map<String, Object> result = new HashMap<>();

        // Build settings from user-defined
        themeSettings.forEach(themeSetting -> {
            String key = themeSetting.getSettingKey();

            Item item = itemMap.get(key);

            if (item == null) {
                return;
            }

            Object convertedValue = item.getDataType().convertTo(themeSetting.getSettingValue());
            log.debug("Converted user-defined data from [{}] to [{}], type: [{}]", themeSetting.getSettingValue(), convertedValue, item.getDataType());

            result.put(key, convertedValue);
        });

        // Build settings from pre-defined
        itemMap.forEach((name, item) -> {
            log.debug("Name: [{}], item: [{}]", name, item);

            if (item.getDefaultValue() == null || result.containsKey(name)) {
                return;
            }

            // 设置默认值
            Object convertedDefaultValue = item.getDataType().convertTo(item.getDefaultValue());
            log.debug("Converted pre-defined data from [{}] to [{}], type: [{}]", item.getDefaultValue(), convertedDefaultValue, item.getDataType());

            result.put(name, convertedDefaultValue);
        });

        return result;
    }

    @Override
    public boolean save(Map<String, Object> settings) {

        if (CollectionUtils.isEmpty(settings)) {
            return false;
        }
        // 保存配置
        settings.forEach((key, value) -> this.saveItem(key, String.valueOf(value)));

        try {
            configuration.setSharedVariable("settings", listAsMap());
        } catch (TemplateModelException e) {
            throw new ServiceException("主题设置保存失败", e);
        }
        return false;
    }

    /**
     * 保存配置
     *
     * @param key   key
     * @param value value
     */
    private void saveItem(String key, String value) {
        if (StringUtils.isBlank(value)) {
            themeSettingMapper.deleteByKey(key);
        }

        ThemeSetting dbThemeSetting = themeSettingMapper.selectByKey(key);

        ThemeSetting themeSetting = new ThemeSetting();
        themeSetting.setSettingKey(key);
        themeSetting.setSettingValue(value);

        if (ObjectUtils.isEmpty(dbThemeSetting)) {
            // 不存在，执行保存
            themeSettingMapper.insert(themeSetting);
        } else {
            // 存在，判断是否已经需要更新，然后执行过更新。
            String settingValue = dbThemeSetting.getSettingValue();
            if (!settingValue.equals(value)) {
                boolean b = themeSettingMapper.updateByKey(themeSetting);
                log.debug("更新主题配置：key：{}，更新结果：{}", key, b);
            }
        }

    }


    @Override
    public Map<String, Object> listAsMap() {
        // Convert to item map(key: item name, value: item)
        Map<String, Item> itemMap = getConfigItemMap();

        // Get theme setting
        List<ThemeSetting> themeSettings = themeSettingMapper.selectList(Wrappers.emptyWrapper());

        Map<String, Object> result = new HashMap<>();

        // Build settings from user-defined
        themeSettings.forEach(themeSetting -> {
            String key = themeSetting.getSettingKey();

            Item item = itemMap.get(key);

            if (item == null) {
                return;
            }

            Object convertedValue = item.getDataType().convertTo(themeSetting.getSettingValue());
            log.debug("Converted user-defined data from [{}] to [{}], type: [{}]", themeSetting.getSettingValue(), convertedValue, item.getDataType());

            result.put(key, convertedValue);
        });

        // Build settings from pre-defined
        itemMap.forEach((name, item) -> {
            log.debug("Name: [{}], item: [{}]", name, item);

            if (item.getDefaultValue() == null || result.containsKey(name)) {
                return;
            }

            // Set default value
            Object convertedDefaultValue = item.getDataType().convertTo(item.getDefaultValue());
            log.debug("Converted pre-defined data from [{}] to [{}], type: [{}]", item.getDefaultValue(), convertedDefaultValue, item.getDataType());

            result.put(name, convertedDefaultValue);
        });

        return result;
    }

    /**
     * 获取配置项映射。 （键：项目名称，值：项目）
     *
     * @return config item map
     */
    private Map<String, Item> getConfigItemMap() {
        // Get theme configuration
        List<Group> groups = themeService.listConfig();

        // Mix all items
        Set<Item> items = new LinkedHashSet<>();
        groups.forEach(group -> items.addAll(group.getItems()));

        // Convert to item map(key: item name, value: item)
        return ServiceUtils.convertToMap(items, Item::getName);
    }


}
