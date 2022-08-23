package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qiniu.storage.Region;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.MissingPropertyException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.event.options.ConfigUpdatedEvent;
import com.qinweizhao.blog.mapper.ConfigMapper;
import com.qinweizhao.blog.model.convert.ConfigConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigSimpleDTO;
import com.qinweizhao.blog.model.entity.Config;
import com.qinweizhao.blog.model.enums.ValueEnum;
import com.qinweizhao.blog.model.param.ConfigParam;
import com.qinweizhao.blog.model.param.ConfigQueryParam;
import com.qinweizhao.blog.model.properties.*;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.util.DateUtils;
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
import java.util.*;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * OptionService implementation class
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
        StringBuilder fullPath = new StringBuilder();

        if (this.isEnabledAbsolutePath()) {
            fullPath.append(this.getBlogBaseUrl());
        }

        fullPath.append(URL_SEPARATOR);

        fullPath.append("?p=")
                .append(postId);

        return fullPath.toString();
    }


    @PostConstruct
    private void init() {
        propertyEnumMap = Collections.unmodifiableMap(PropertyEnum.getValuePropertyEnumMap());
    }

    @SuppressWarnings("all")
    @Override
    public @NotNull Map<String, Object> listOptions() {
        return cacheStore.getAny(OPTIONS_KEY, Map.class).orElseGet(() -> {
            List<Config> configs = configMapper.selectList(Wrappers.emptyWrapper());

            Set<String> keys = ServiceUtils.fetchProperty(configs, Config::getOptionKey);

            Map<String, Object> userDefinedOptionMap = ServiceUtils.convertToMap(configs, Config::getOptionKey, config -> {
                String key = config.getOptionKey();

                PropertyEnum propertyEnum = propertyEnumMap.get(key);

                if (propertyEnum == null) {
                    return config.getOptionValue();
                }

                return PropertyEnum.convertTo(config.getOptionValue(), propertyEnum);
            });

            Map<String, Object> result = new HashMap<>(userDefinedOptionMap);

            propertyEnumMap.keySet()
                    .stream()
                    .filter(key -> !keys.contains(key))
                    .forEach(key -> {
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

        keys.stream()
                .filter(optionMap::containsKey)
                .forEach(key -> result.put(key, optionMap.get(key)));

        return result;
    }


    @Override
    public Object getByKeyOfNullable(String key) {
        return getByKey(key).orElse(null);
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
    public Object getByPropertyOfNullable(PropertyEnum property) {
        return getByProperty(property).orElse(null);
    }

    @Override
    public Object getByPropertyOfNonNull(PropertyEnum property) {
        Assert.notNull(property, "Blog property must not be null");

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
    public <T> T getByKeyOrDefault(String key, Class<T> valueType, T defaultValue) {
        return getByKey(key, valueType).orElse(defaultValue);
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
    public <V, E extends ValueEnum<V>> Optional<E> getValueEnumByProperty(PropertyEnum property, Class<V> valueType, Class<E> enumType) {
        return getByProperty(property).map(value -> ValueEnum.valueToEnum(enumType, PropertyEnum.convertTo(value.toString(), valueType)));
    }

    @Override
    public <V, E extends ValueEnum<V>> E getValueEnumByPropertyOrDefault(PropertyEnum property, Class<V> valueType, Class<E> enumType, E defaultValue) {
        return getValueEnumByProperty(property, valueType, enumType).orElse(defaultValue);
    }

    @Override
    public int getPostPageSize() {
        try {
            return getByPropertyOrDefault(PostProperties.INDEX_PAGE_SIZE, Integer.class, DEFAULT_POST_PAGE_SIZE);
        } catch (NumberFormatException e) {
            log.error(PostProperties.INDEX_PAGE_SIZE.getValue() + " option is not a number format", e);
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
                    // Default is detecting zone automatically
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
    public String getSheetPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.SHEET_PREFIX, String.class, PermalinkProperties.SHEET_PREFIX.defaultValue());
    }

    @Override
    public String getLinksPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.LINKS_PREFIX, String.class, PermalinkProperties.LINKS_PREFIX.defaultValue());
    }

    @Override
    public String getPhotosPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.PHOTOS_PREFIX, String.class, PermalinkProperties.PHOTOS_PREFIX.defaultValue());
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
    public String getPathSuffix() {
        return getByPropertyOrDefault(PermalinkProperties.PATH_SUFFIX, String.class, PermalinkProperties.PATH_SUFFIX.defaultValue());
    }

    @Override
    public boolean isEnabledAbsolutePath() {
        return getByPropertyOrDefault(OtherProperties.GLOBAL_ABSOLUTE_PATH_ENABLED, Boolean.class, true);
    }

    @Override
    public long getBirthday() {
        return getByProperty(PrimaryProperties.BIRTHDAY, Long.class).orElseGet(() -> {
            long currentTime = DateUtils.now().getTime();
            this.saveProperty(PrimaryProperties.BIRTHDAY, String.valueOf(currentTime));
            return currentTime;
        });
    }

    @Override
    public PageResult<ConfigSimpleDTO> pageSimple(ConfigQueryParam param) {
        PageResult<Config> page = configMapper.selectPage(param);
        return ConfigConvert.INSTANCE.convert(page);
    }

    @Override
    public boolean save(ConfigParam param) {
        String key = param.getKey();
        String value = param.getValue();
        Config dbConfig = configMapper.selectByKey(key);
        if (ObjectUtils.isEmpty(dbConfig)) {
            Config config = ConfigConvert.INSTANCE.convert(param);
            configMapper.insert(config);
            this.publishOptionUpdatedEvent();
            return true;
        } else {
            if (dbConfig.getOptionValue().equals(value)) {
                return true;
            } else {
                Config config = ConfigConvert.INSTANCE.convert(param);
                config.setId(dbConfig.getId());
                configMapper.updateById(config);
            }
        }
        this.publishOptionUpdatedEvent();
        return true;
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
    public void saveProperty(PropertyEnum property, String value) {
        this.save(Collections.singletonMap(property.getValue(), value));

    }

    @Override
    public void save(Map<String, Object> optionMap) {
        if (CollectionUtils.isEmpty(optionMap)) {
            return;
        }

        Map<String, Config> optionKeyMap = ServiceUtils.convertToMap(configMapper.selectList(Wrappers.emptyWrapper()), Config::getOptionKey);

        List<Config> optionsToCreate = new LinkedList<>();
        List<Config> optionsToUpdate = new LinkedList<>();

        optionMap.forEach((key, value) -> {
            if (ObjectUtils.isEmpty(value)) {
                int i = configMapper.deleteByKey(key);
                log.debug("删除配置，key 为：{}，结果是否成功{}", key, i > 0);
            }

            Config oldConfig = optionKeyMap.get(key);
            if (oldConfig == null || !StringUtils.equals(oldConfig.getOptionValue(), value.toString())) {

                Config config = new Config();
                config.setOptionKey(key);
                config.setOptionValue(String.valueOf(value));

                if (oldConfig == null) {
                    optionsToCreate.add(config);
                } else if (!StringUtils.equals(oldConfig.getOptionValue(), value.toString())) {
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
}

