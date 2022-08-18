package com.qinweizhao.blog.framework.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Cache store interface.
 *
 * @param <K> cache key type
 * @param <V> cache value type
 * @author johnniang
 * *
 */
public interface CacheStore<K, V> {

    /**
     * Gets by cache key.
     *
     * @param key must not be null
     * @return cache value
     */
    Optional<V> get(K key);

    /**
     * Puts a cache which will be expired.
     *
     * @param key      cache key must not be null
     * @param value    cache value must not be null
     * @param timeout  the key expiration must not be less than 1
     * @param timeUnit timeout unit
     */
    void put(K key, V value, long timeout, TimeUnit timeUnit);

    /**
     * Puts a cache which will be expired if the key is absent.
     *
     * @param key      cache key must not be null
     * @param value    cache value must not be null
     * @param timeout  the key expiration must not be less than 1
     * @param timeUnit timeout unit must not be null
     * @return true if the key is absent and the value is set, false if the key is present before, or null if any other reason
     */
    Boolean putIfAbsent(K key, V value, long timeout, TimeUnit timeUnit);

    /**
     * Puts a non-expired cache.
     *
     * @param key   cache key must not be null
     * @param value cache value must not be null
     */
    void put(K key, V value);

    /**
     * Delete a key.
     *
     * @param key cache key must not be null
     */
    void delete(K key);

}
