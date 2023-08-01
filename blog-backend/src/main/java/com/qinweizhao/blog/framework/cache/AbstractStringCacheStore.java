package com.qinweizhao.blog.framework.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * String cache store.
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
@Slf4j
public abstract class AbstractStringCacheStore extends AbstractCacheStore<String, String> {

    protected Optional<CacheWrapper<String>> jsonToCacheWrapper(String json) {
        Assert.hasText(json, "json value must not be null");
        CacheWrapper<String> cacheWrapper = null;
        try {
            cacheWrapper = JsonUtils.jsonToObject(json, CacheWrapper.class);
        } catch (IOException e) {
            log.debug("Failed to convert json to wrapper value bytes: [{}]", json, e);
        }
        return Optional.ofNullable(cacheWrapper);
    }

    public <T> void putAny(String key, T value) {
        try {
            put(key, JsonUtils.objectToJson(value));
        } catch (JsonProcessingException e) {
            throw new ServiceException("Failed to convert " + value + " to json", e);
        }
    }

    public <T> void putAny(@NonNull String key, @NonNull T value, long timeout, @NonNull TimeUnit timeUnit) {
        try {
            put(key, JsonUtils.objectToJson(value), timeout, timeUnit);
        } catch (JsonProcessingException e) {
            throw new ServiceException("Failed to convert " + value + " to json", e);
        }
    }

    /**
     * 获取信息
     *
     * @param key  key
     * @param type type
     * @param <T>  T
     * @return T
     */
    public <T> Optional<T> getAny(String key, Class<T> type) {
        return get(key).map(value -> {
            try {
                return JsonUtils.jsonToObject(value, type);
            } catch (IOException e) {
                log.error("无法将 Json 转换为类型: " + type.getName(), e);
                return null;
            }
        });
    }
}
