package com.qinweizhao.blog.framework.cache;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Cache wrapper.
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
class CacheWrapper<V> implements Serializable {

    /**
     * Cache data
     */
    private V data;

    /**
     * Expired time.
     */
    private Date expireAt;

    /**
     * Create time.
     */
    private Date createAt;
}
