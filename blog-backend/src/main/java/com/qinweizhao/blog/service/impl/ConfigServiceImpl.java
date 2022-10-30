package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qiniu.storage.Region;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.MissingPropertyException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.event.config.ConfigUpdatedEvent;
import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.Item;
import com.qinweizhao.blog.mapper.ConfigMapper;
import com.qinweizhao.blog.model.convert.ConfigConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigSimpleDTO;
import com.qinweizhao.blog.model.entity.Config;
import com.qinweizhao.blog.model.enums.ConfigType;
import com.qinweizhao.blog.model.param.ConfigParam;
import com.qinweizhao.blog.model.param.ConfigQueryParam;
import com.qinweizhao.blog.model.properties.*;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.ThemeService;
import com.qinweizhao.blog.util.ServiceUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.qinweizhao.blog.model.support.BlogConst.URL_SEPARATOR;

/**
 * OptionService 实现类
 *
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private final ApplicationContext applicationContext;

    private final AbstractStringCacheStore cacheStore;
    private final ApplicationEventPublisher eventPublisher;
    private final MyBlogProperties myBlogProperties;
    private final ConfigMapper configMapper;
    private Map<String, PropertyEnum> propertyEnumMap;


    @Override
    public String buildFullPath(Integer postId) {
        return this.getBlogBaseUrl() + URL_SEPARATOR + this.getArticlePrefix() + URL_SEPARATOR + postId;
    }


    @PostConstruct
    private void init() {
        propertyEnumMap = Collections.unmodifiableMap(PropertyEnum.getValuePropertyEnumMap());
    }

    @SuppressWarnings("all")
    @Override
    public @NotNull Map<String, Object> listOptions() {
        return cacheStore.getAny(OPTIONS_KEY, Map.class).orElseGet(() -> {
            List<Config> configs = configMapper.selectListByType(ConfigType.ADMIN);

            Set<String> keys = ServiceUtils.fetchProperty(configs, Config::getConfigKey);

            Map<String, Object> userDefinedOptionMap = ServiceUtils.convertToMap(configs, Config::getConfigKey, config -> {
                String key = config.getConfigKey();

                PropertyEnum propertyEnum = propertyEnumMap.get(key);

                if (propertyEnum == null) {
                    return config.getConfigValue();
                }

                return PropertyEnum.convertTo(config.getConfigValue(), propertyEnum);
            });

            Map<String, Object> result = new HashMap<>(userDefinedOptionMap);

            propertyEnumMap.keySet().stream().filter(key -> !keys.contains(key)).forEach(key -> {
                PropertyEnum propertyEnum = propertyEnumMap.get(key);

                if (StringUtils.isBlank(propertyEnum.defaultValue())) {
                    return;
                }

                result.put(key, PropertyEnum.convertTo(propertyEnum.defaultValue(), propertyEnum));
            });

            // 补充博客地址属性
            result.put("blog_url", this.getBlogBaseUrl());
            cacheStore.putAny(OPTIONS_KEY, result);

            return result;
        });
    }

    @Override
    public Map<String, Object> listOptions(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        Map<String, Object> optionMap = listOptions();

        Map<String, Object> result = new HashMap<>(keys.size());

        keys.stream().filter(optionMap::containsKey).forEach(key -> result.put(key, optionMap.get(key)));

        return result;
    }


    @Override
    public Object getByKeyOfNonNull(String key) {
        return getByKey(key).orElseThrow(() -> new MissingPropertyException("You have to config " + key + " setting"));
    }

    @Override
    public Optional<Object> getByKey(String key) {
        Assert.hasText(key, "Option key must not be blank");

        return Optional.ofNullable(listOptions().get(key));
    }

    @Override
    public Object getByPropertyOfNonNull(PropertyEnum property) {
        Assert.notNull(property, "属性不能为空");

        return getByKeyOfNonNull(property.getValue());
    }

    @Override
    public Optional<Object> getByProperty(PropertyEnum property) {
        Assert.notNull(property, "Blog property must not be null");

        return getByKey(property.getValue());
    }

    @Override
    public <T> T getByPropertyOrDefault(PropertyEnum property, Class<T> propertyType, T defaultValue) {
        Assert.notNull(property, "Blog property must not be null");

        return getByProperty(property, propertyType).orElse(defaultValue);
    }

    @Override
    public <T> T getByPropertyOrDefault(PropertyEnum property, Class<T> propertyType) {
        return getByProperty(property, propertyType).orElse(property.defaultValue(propertyType));
    }

    @Override
    public <T> Optional<T> getByProperty(PropertyEnum property, Class<T> propertyType) {
        return getByProperty(property).map(propertyValue -> PropertyEnum.convertTo(propertyValue.toString(), propertyType));
    }


    @Override
    public <T> Optional<T> getByKey(String key, Class<T> valueType) {
        return getByKey(key).map(value -> PropertyEnum.convertTo(value.toString(), valueType));
    }

    @Override
    public <T extends Enum<T>> Optional<T> getEnumByProperty(PropertyEnum property, Class<T> valueType) {
        return getByProperty(property).map(value -> PropertyEnum.convertToEnum(value.toString(), valueType));
    }

    @Override
    public <T extends Enum<T>> T getEnumByPropertyOrDefault(PropertyEnum property, Class<T> valueType, T defaultValue) {
        return getEnumByProperty(property, valueType).orElse(defaultValue);
    }


    @Override
    public int getPostPageSize() {
        try {
            return getByPropertyOrDefault(PostProperties.INDEX_PAGE_SIZE, Integer.class, DEFAULT_POST_PAGE_SIZE);
        } catch (NumberFormatException e) {
            log.error(PostProperties.INDEX_PAGE_SIZE.getValue() + "配置不是数字格式", e);
            return DEFAULT_POST_PAGE_SIZE;
        }
    }

    @Override
    public int getArchivesPageSize() {
        try {
            return getByPropertyOrDefault(PostProperties.ARCHIVES_PAGE_SIZE, Integer.class, DEFAULT_ARCHIVES_PAGE_SIZE);
        } catch (NumberFormatException e) {
            log.error(PostProperties.ARCHIVES_PAGE_SIZE.getValue() + " option is not a number format", e);
            return DEFAULT_POST_PAGE_SIZE;
        }
    }

    @Override
    public int getCommentPageSize() {
        try {
            return getByPropertyOrDefault(CommentProperties.PAGE_SIZE, Integer.class, DEFAULT_COMMENT_PAGE_SIZE);
        } catch (NumberFormatException e) {
            log.error(CommentProperties.PAGE_SIZE.getValue() + " option is not a number format", e);
            return DEFAULT_COMMENT_PAGE_SIZE;
        }
    }

    @Override
    public Region getQiniuRegion() {
        return getByProperty(QiniuOssProperties.OSS_ZONE).map(qiniuZone -> {

            Region region;
            switch (qiniuZone.toString()) {
                case "z0":
                    region = Region.region0();
                    break;
                case "z1":
                    region = Region.region1();
                    break;
                case "z2":
                    region = Region.region2();
                    break;
                case "na0":
                    region = Region.regionNa0();
                    break;
                case "as0":
                    region = Region.regionAs0();
                    break;
                default:
                    // 默认是自动检测区域
                    region = Region.autoRegion();
            }
            return region;

        }).orElseGet(Region::autoRegion);
    }

    @Override
    public Locale getLocale() {
        return getByProperty(BlogProperties.BLOG_LOCALE).map(localeStr -> {
            try {
                return Locale.forLanguageTag(localeStr.toString());
            } catch (Exception e) {
                return Locale.getDefault();
            }
        }).orElseGet(Locale::getDefault);
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
    public String getBlogTitle() {
        return getByProperty(BlogProperties.BLOG_TITLE).orElse("").toString();
    }

    @Override
    public String getSeoKeywords() {
        return getByProperty(SeoProperties.KEYWORDS).orElse("").toString();
    }

    @Override
    public String getSeoDescription() {
        return getByProperty(SeoProperties.DESCRIPTION).orElse("").toString();
    }


    @Override
    public String getJournalsPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.JOURNALS_PREFIX, String.class, PermalinkProperties.JOURNALS_PREFIX.defaultValue());
    }

    @Override
    public String getArchivesPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.ARCHIVES_PREFIX, String.class, PermalinkProperties.ARCHIVES_PREFIX.defaultValue());
    }

    @Override
    public String getCategoriesPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.CATEGORIES_PREFIX, String.class, PermalinkProperties.CATEGORIES_PREFIX.defaultValue());
    }

    @Override
    public String getTagsPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.TAGS_PREFIX, String.class, PermalinkProperties.TAGS_PREFIX.defaultValue());
    }

    @Override
    public String getArticlePrefix() {
        return getByPropertyOrDefault(PermalinkProperties.ARTICLE_PREFIX, String.class, PermalinkProperties.TAGS_PREFIX.defaultValue());

    }


    @Override
    public PageResult<ConfigSimpleDTO> pageSimple(ConfigQueryParam param) {
        PageResult<Config> page = configMapper.selectPage(param);
        return ConfigConvert.INSTANCE.convert(page);
    }

    @Override
    public boolean save(ConfigParam param) {
        boolean flag = false;
        String key = param.getKey();
        String value = param.getValue();
        Config dbConfig = configMapper.selectByKey(key);
        if (ObjectUtils.isEmpty(dbConfig)) {
            Config config = ConfigConvert.INSTANCE.convert(param);
            configMapper.insert(config);
            this.publishOptionUpdatedEvent();
            flag = true;
        } else {
            if (dbConfig.getConfigValue().equals(value)) {
                flag = true;
            } else {
                Config config = ConfigConvert.INSTANCE.convert(param);
                config.setId(dbConfig.getId());
                configMapper.updateById(config);
            }
        }
        this.publishOptionUpdatedEvent();
        return flag;
    }

    @Override
    public boolean updateById(ConfigParam param) {
        Config config = ConfigConvert.INSTANCE.convert(param);
        int flag = configMapper.updateById(config);
        if (flag > 0) {
            this.publishOptionUpdatedEvent();
        }
        return flag > 0;
    }

    @Override
    public boolean removeById(Integer optionId) {
        int flag = configMapper.deleteById(optionId);
        if (flag > 0) {
            this.publishOptionUpdatedEvent();
        }
        return flag > 0;
    }

    @Override
    public void save(Map<String, Object> optionMap) {
        if (CollectionUtils.isEmpty(optionMap)) {
            return;
        }

        Map<String, Config> optionKeyMap = ServiceUtils.convertToMap(configMapper.selectList(Wrappers.emptyWrapper()), Config::getConfigValue);

        List<Config> optionsToCreate = new LinkedList<>();
        List<Config> optionsToUpdate = new LinkedList<>();

        optionMap.forEach((key, value) -> {
            if (ObjectUtils.isEmpty(value)) {
                int i = configMapper.deleteByKey(key);
                log.debug("删除配置，key 为：{}，结果是否成功{}", key, i > 0);
            }

            Config oldConfig = optionKeyMap.get(key);
            if (oldConfig == null || !StringUtils.equals(oldConfig.getConfigValue(), value.toString())) {

                Config config = new Config();
                config.setConfigKey(key);
                config.setConfigValue(String.valueOf(value));

                if (oldConfig == null) {
                    optionsToCreate.add(config);
                } else if (!StringUtils.equals(oldConfig.getConfigValue(), value.toString())) {
                    config.setId(oldConfig.getId());
                    optionsToUpdate.add(config);
                }
            }
        });

        boolean updateFlag = configMapper.updateBatchById(optionsToUpdate);

        boolean insertFlag = configMapper.insertBatch(optionsToCreate);

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

    /**
     * 发布配置更新事件
     */
    private void publishOptionUpdatedEvent() {
        log.debug("配置变动，清除缓存开始。");
        cleanCache();
        log.debug("配置变动，清除缓存完成。");
        eventPublisher.publishEvent(new ConfigUpdatedEvent(this));
    }
    // =================== start===========================//

    private final ThemeService themeService;

    private final Configuration configuration;

    @Override
    public Map<String, Object> getSettings() {

        Map<String, Item> itemMap = getConfigItemMap();

        // 获取主题配置
        List<Config> themeSettings = configMapper.selectListByType(ConfigType.PORTAL);

        Map<String, Object> result = new LinkedHashMap<>();

        // 从用户定义的构建设置
        themeSettings.forEach(themeSetting -> {
            String key = themeSetting.getConfigKey();

            Item item = itemMap.get(key);

            if (item == null) {
                return;
            }

            Object convertedValue = item.getDataType().convertTo(themeSetting.getConfigValue());
            log.debug("将用户定义的数据从 [{}] 转换为 [{}], 类型: [{}]", themeSetting.getConfigValue(), convertedValue, item.getDataType());

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

        return result;
    }

    @Override
    public boolean save(Map<String, Object> settings, ConfigType type) {

        if (CollectionUtils.isEmpty(settings)) {
            return false;
        }
        // 保存配置
        settings.forEach((key, value) -> this.saveItem(key, String.valueOf(value), type));

        try {
            configuration.setSharedVariable("settings", this.getSettings());
        } catch (TemplateModelException e) {
            throw new ServiceException("主题设置保存失败", e);
        }

        return true;
    }

    /**
     * 保存配置
     *
     * @param key   key
     * @param value value
     */
    private void saveItem(String key, String value, ConfigType type) {
        if (ObjectUtils.isEmpty(value)) {
            log.debug("主题配置");
            int i = configMapper.deleteByKey(key);
            log.debug("删除主题配置{}条，key:{}", i, key);
            return;
        }

        Config dbThemeSetting = configMapper.selectByKey(key);

        Config themeSetting = new Config();
        themeSetting.setConfigKey(key);
        themeSetting.setConfigValue(value);
        themeSetting.setType(type.getValue());

        if (ObjectUtils.isEmpty(dbThemeSetting)) {
            log.debug("主题配置");
            log.debug("保存主题配置，key:{}", key);

            // 不存在，执行保存
            configMapper.insert(themeSetting);
        } else {
            // 存在，判断是否已经需要更新，然后执行过更新。
            String settingValue = dbThemeSetting.getConfigValue();
            if (!settingValue.equals(value)) {
                boolean b = configMapper.updateByKey(themeSetting);
                log.debug("主题配置");
                log.debug("更新主题配置：key：{}，更新结果：{}", key, b);
            }
        }

    }

    /**
     * 获取配置项映射。 （键：项目名称，值：项目）
     *
     * @return config item map
     */
    private Map<String, Item> getConfigItemMap() {

        List<Group> groups = themeService.listConfig();

        Set<Item> items = new LinkedHashSet<>();
        groups.forEach(group -> items.addAll(group.getItems()));

        return ServiceUtils.convertToMap(items, Item::getName);
    }
//==============end=============//
}

