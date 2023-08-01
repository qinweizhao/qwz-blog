package com.qinweizhao.blog.framework.cache;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Cache wrapper.
 *
 * @author qinweizhao
 * @since 2019-03-17
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
