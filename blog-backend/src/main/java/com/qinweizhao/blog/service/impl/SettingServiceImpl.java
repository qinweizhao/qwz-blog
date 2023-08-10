package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.event.config.ConfigUpdatedEvent;
import com.qinweizhao.blog.framework.handler.theme.config.ThemeConfigResolver;
import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.Item;
import com.qinweizhao.blog.mapper.SettingMapper;
import com.qinweizhao.blog.model.constant.SystemConstant;
import com.qinweizhao.blog.model.convert.SettingConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigDTO;
import com.qinweizhao.blog.model.entity.Setting;
import com.qinweizhao.blog.model.param.SettingParam;
import com.qinweizhao.blog.model.param.SettingQueryParam;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.util.FileUtils;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.qinweizhao.blog.model.support.BlogConst.URL_SEPARATOR;

/**
 * OptionService 实现类
 *
 * @author qinweizhao
 * @since 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final ApplicationContext applicationContext;

    private final AbstractStringCacheStore cacheStore;
    private final ApplicationEventPublisher eventPublisher;
    private final MyBlogProperties myBlogProperties;
    private final SettingMapper settingMapper;
    private final ThemeConfigResolver themeConfigResolver;


    @Override
    public String buildFullPath(Integer articleId) {
        return this.getBlogBaseUrl() + URL_SEPARATOR + SystemConstant.ARTICLE_PREFIX + URL_SEPARATOR + articleId;
    }


    @Override
    public Map<String, Object> getMap(List<String> keys) {
        return this.listOptions(keys);
    }

    @Override
    public Map<String, Object> listOptions(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        Map<String, Object> optionMap = getSettings();

        Map<String, Object> result = new HashMap<>(keys.size());

        keys.stream().filter(optionMap::containsKey).forEach(key -> result.put(key, optionMap.get(key)));

        return result;
    }


    @Override
    public Object get(String key) {
        return this.getSettings().get(key);
    }


    @Override
    public int getPostPageSize() {
        try {
            Object postIndexPageSize = this.get("post_index_page_size");
            return Integer.parseInt(String.valueOf(postIndexPageSize == null ? 10 : postIndexPageSize));
        } catch (NumberFormatException e) {
            log.error("配置不是数字格式", e);
            return DEFAULT_POST_PAGE_SIZE;
        }
    }


    @Override
    public String getBlogBaseUrl() {
        // Get server port
        String serverPort = applicationContext.getEnvironment().getProperty("server.port", "8080");

        String blogUrl = myBlogProperties.getBlogUrl();

        if (StringUtils.isNotBlank(blogUrl)) {
            blogUrl = StringUtils.removeEnd(blogUrl, "/");
        } else {
            blogUrl = String.format("http://%s:%s", "127.0.0.1", serverPort);
        }

        return blogUrl;
    }


    @Override
    public PageResult<ConfigDTO> pageSimple(SettingQueryParam param) {
        PageResult<Setting> page = settingMapper.selectPage(param);
        return SettingConvert.INSTANCE.convert(page);
    }

    @Override
    public boolean save(SettingParam param) {
        boolean flag = false;
        String key = param.getKey();
        String value = param.getValue();
        Setting dbSetting = settingMapper.selectByKey(key);
        if (ObjectUtils.isEmpty(dbSetting)) {
            Setting setting = SettingConvert.INSTANCE.convert(param);
            settingMapper.insert(setting);
            this.publishOptionUpdatedEvent();
            flag = true;
        } else {
            if (dbSetting.getSettingValue().equals(value)) {
                flag = true;
            } else {
                Setting setting = SettingConvert.INSTANCE.convert(param);
                setting.setId(dbSetting.getId());
                settingMapper.updateById(setting);
            }
        }
        this.publishOptionUpdatedEvent();
        return flag;
    }

    @Override
    public boolean updateById(SettingParam param) {
        Setting setting = SettingConvert.INSTANCE.convert(param);
        int flag = settingMapper.updateById(setting);
        if (flag > 0) {
            this.publishOptionUpdatedEvent();
        }
        return flag > 0;
    }

    @Override
    public boolean removeById(Integer optionId) {
        int flag = settingMapper.deleteById(optionId);
        if (flag > 0) {
            this.publishOptionUpdatedEvent();
        }
        return flag > 0;
    }

    @Override
    public void save(Map<String, Object> configMap) {
        if (CollectionUtils.isEmpty(configMap)) {
            return;
        }


        Map<String, Setting> optionKeyMap = ServiceUtils.convertToMap(settingMapper.selectList(Wrappers.emptyWrapper()), Setting::getSettingKey);

        List<Setting> optionsToCreate = new LinkedList<>();
        List<Setting> optionsToUpdate = new LinkedList<>();

        configMap.forEach((key, value) -> {
            if (ObjectUtils.isEmpty(value)) {
                int i = settingMapper.deleteByKey(key);
                log.debug("删除配置，key 为：{}，结果是否成功{}", key, i > 0);
            }

            Setting oldSetting = optionKeyMap.get(key);
            if (oldSetting == null || !StringUtils.equals(oldSetting.getSettingValue(), value.toString())) {

                Setting setting = new Setting();
                setting.setSettingKey(key);
                setting.setSettingValue(String.valueOf(value));


                if (oldSetting == null) {
                    optionsToCreate.add(setting);
                } else if (!StringUtils.equals(oldSetting.getSettingValue(), value.toString())) {
                    setting.setId(oldSetting.getId());
                    optionsToUpdate.add(setting);
                }
            }
        });

        boolean updateFlag = settingMapper.updateBatchById(optionsToUpdate);

        boolean insertFlag = settingMapper.insertBatch(optionsToCreate);

        log.debug("更新配置{}，新增配置{}", updateFlag, insertFlag);

        if (!CollectionUtils.isEmpty(optionsToUpdate) || !CollectionUtils.isEmpty(optionsToCreate)) {
            // 如果有什么改变
            this.publishOptionUpdatedEvent();
        }

    }

    /**
     * 清除缓存
     */
    private void cleanCache() {
        cacheStore.delete(OPTIONS_KEY);
    }


    // =================== start===========================//

    /**
     * 发布配置更新事件
     */
    private void publishOptionUpdatedEvent() {
        log.debug("配置变动，清除缓存开始。");
        cleanCache();
        log.debug("配置变动，清除缓存完成。");
        eventPublisher.publishEvent(new ConfigUpdatedEvent(this));
    }

    @Override
    public Map<String, Object> getSettings() {
        return cacheStore.getAny(OPTIONS_KEY, Map.class).orElseGet(() -> {
            Map<String, Item> itemMap = getConfigItemMap();

            // 获取主题配置
            List<Setting> themeSettings = settingMapper.selectList(Wrappers.emptyWrapper());

            Map<String, Object> result = new LinkedHashMap<>();

            // 从用户定义的构建设置
            themeSettings.forEach(themeSetting -> {
                String key = themeSetting.getSettingKey();

                Item item = itemMap.get(key);

                if (item == null) {
                    return;
                }

                Object convertedValue = item.getDataType().convertTo(themeSetting.getSettingValue());
                log.debug("将用户定义的数据从 [{}] 转换为 [{}], 类型: [{}]", themeSetting.getSettingValue(), convertedValue, item.getDataType());

                result.put(key, convertedValue);
            });

            // 从预定义的构建设置
            itemMap.forEach((name, item) -> {
                log.debug("Name: [{}], item: [{}]", name, item);

                if (item.getDefaultValue() == null || result.containsKey(name)) {
                    return;
                }

                // 设置默认值
                Object convertedDefaultValue = item.getDataType().convertTo(item.getDefaultValue());
                log.debug("将预定义数据来自 [{}] 转换为 [{}], 类型: [{}]", item.getDefaultValue(), convertedDefaultValue, item.getDataType());

                result.put(name, convertedDefaultValue);
            });

            result.put("blog_url",getBlogBaseUrl());
            return result;
        });
    }

    /**
     * 获取配置项映射。 （键：项目名称，值：项目）
     *
     * @return config item map
     */
    private Map<String, Item> getConfigItemMap() {

        List<Group> groups = this.listConfig();

        Set<Item> items = new LinkedHashSet<>();
        groups.forEach(group -> items.addAll(group.getItems()));

        return ServiceUtils.convertToMap(items, Item::getName);
    }


    /**
     * @return Path
     */
    public Path getBasePath() {
        String frontendDirName = myBlogProperties.getFrontendDirName();
        String themeDirName = myBlogProperties.getThemeDirName();
        Path frontend = Paths.get(myBlogProperties.getWorkDir(), frontendDirName, themeDirName);
        log.debug("将要扫描的目录为：{}", frontend);
        return frontend;
    }

    @Override
    public List<Group> listConfig() {
        try {
            for (String optionsName : SETTINGS_NAMES) {
                // Resolve the options path
                Path optionsPath = Paths.get(this.getBasePath().toString(), optionsName);

                log.debug("查找主题配置文件: [{}]", optionsPath);

                // Check existence
                if (!Files.exists(optionsPath)) {
                    continue;
                }

                // Read the yaml file
                String optionContent = new String(Files.readAllBytes(optionsPath), StandardCharsets.UTF_8);

                // Resolve it
                return themeConfigResolver.resolve(optionContent);
            }

            return Collections.emptyList();
        } catch (IOException e) {
            throw new ServiceException("读取主题配置文件失败", e);
        }
    }


    @Override
    public String render(String pageName) {
        return String.format(RENDER_TEMPLATE, this.getBasePath().getFileName().toString(), pageName);
    }

    @Override
    public String renderWithSuffix(String pageName) {
        // 构建渲染地址
        return String.format(RENDER_TEMPLATE_SUFFIX, this.getBasePath().getFileName().toString(), pageName);
    }

    @Override
    public boolean templateExists(String template) {
        if (StringUtils.isBlank(template)) {
            return false;
        }
        Path templatePath = Paths.get(this.getBasePath().toString(), template);
        // Check the directory
        checkDirectory(templatePath.toString());
        // Check existence
        return Files.exists(templatePath);

    }

    /**
     * 检查目录是否有效
     *
     * @param absoluteName must not be blank
     */
    private void checkDirectory(String absoluteName) {
        FileUtils.checkDirectoryTraversal(this.getBasePath().toString(), absoluteName);
    }


}

