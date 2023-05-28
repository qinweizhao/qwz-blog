package com.qinweizhao.blog.service.impl;

import com.qiniu.storage.Region;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.MissingPropertyException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.event.config.ConfigUpdatedEvent;
import com.qinweizhao.blog.framework.handler.theme.config.ThemeConfigResolver;
import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.Item;
import com.qinweizhao.blog.framework.handler.theme.config.support.ThemeProperty;
import com.qinweizhao.blog.mapper.ConfigMapper;
import com.qinweizhao.blog.model.convert.ConfigConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigDTO;
import com.qinweizhao.blog.model.entity.Config;
import com.qinweizhao.blog.model.enums.ConfigType;
import com.qinweizhao.blog.model.param.ConfigParam;
import com.qinweizhao.blog.model.param.ConfigQueryParam;
import com.qinweizhao.blog.model.properties.*;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.ThemeService;
import com.qinweizhao.blog.theme.ThemePropertyScanner;
import com.qinweizhao.blog.util.ServiceUtils;
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
public class ConfigServiceImpl implements ConfigService {

    private final ApplicationContext applicationContext;

    private final AbstractStringCacheStore cacheStore;
    private final ApplicationEventPublisher eventPublisher;
    private final MyBlogProperties myBlogProperties;
    private final ConfigMapper configMapper;
    private final ThemeConfigResolver themeConfigResolver;
    private final ThemeService themeService;
    private Map<String, PropertyEnum> propertyEnumMap;

    @Override
    public String buildFullPath(Integer postId) {
        return this.getBlogBaseUrl() + URL_SEPARATOR + this.getArticlePrefix() + URL_SEPARATOR + postId;
    }

    @PostConstruct
    private void init() {
        propertyEnumMap = Collections.unmodifiableMap(PropertyEnum.getValuePropertyEnumMap());
    }

    @Override
    public @NotNull Map<String, Object> getMap() {
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
    public Map<String, Object> getMap(ConfigType type, List<String> keys) {
        if (ObjectUtils.isEmpty(keys) && !ObjectUtils.isEmpty(type)) {
            if (ConfigType.ADMIN.equals(type)) {
                return this.getMap();
            } else if (ConfigType.PORTAL.equals(type)) {
                return this.getSettings();
            }
        }
        return this.listOptions(keys);
    }

    @Override
    public Map<String, Object> listOptions(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        Map<String, Object> optionMap = getMap();

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

        return Optional.ofNullable(getMap().get(key));
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
    public PageResult<ConfigDTO> pageSimple(ConfigQueryParam param) {
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
    public void save(ConfigType type, Map<String, Object> configMap) {
        if (CollectionUtils.isEmpty(configMap)) {
            return;
        }


        Map<String, Config> optionKeyMap = ServiceUtils.convertToMap(configMapper.selectListByType(type), Config::getConfigKey);

        List<Config> optionsToCreate = new LinkedList<>();
        List<Config> optionsToUpdate = new LinkedList<>();

        configMap.forEach((key, value) -> {
            if (ObjectUtils.isEmpty(value)) {
                int i = configMapper.deleteByKey(key);
                log.debug("删除配置，key 为：{}，结果是否成功{}", key, i > 0);
            }

            Config oldConfig = optionKeyMap.get(key);
            if (oldConfig == null || !StringUtils.equals(oldConfig.getConfigValue(), value.toString())) {

                Config config = new Config();
                config.setConfigKey(key);
                config.setConfigValue(String.valueOf(value));
                config.setType(type.getValue());

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


    @Override
    public ThemeProperty getThemeProperty() {
        return Optional.of(getThemes()).orElseThrow(() -> new NotFoundException(" 主题不存在或已删除！"));
    }

    /**
     * 获取主题配置
     *
     * @return List
     */
    public ThemeProperty getThemes() {
        String themeDirName = myBlogProperties.getThemeDirName();


        return cacheStore.getAny(THEMES_CACHE_KEY, ThemeProperty.class).orElseGet(() -> {
            // 扫描配置，为防止报异常，如果存在多个只会取扫描的第一个。
            ThemeProperty properties = ThemePropertyScanner.INSTANCE.scan(getBasePath(), themeDirName);
            // 缓存主题配置
            log.debug("主题配置{}", properties);
            cacheStore.putAny(THEMES_CACHE_KEY, properties);
            return properties;
        });
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

        // 获取主题属性
        ThemeProperty themeProperty = getThemeProperty();

        if (!themeProperty.isHasOptions()) {
            // If this theme dose not has an option, then return empty list
            return Collections.emptyList();
        }

        try {
            for (String optionsName : SETTINGS_NAMES) {
                // Resolve the options path
                Path optionsPath = Paths.get(themeProperty.getThemePath(), optionsName);

                log.debug("Finding options in: [{}]", optionsPath);

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


//==============end=============//
}

