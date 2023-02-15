package com.qinweizhao.blog.framework.cache.lock;

import java.lang.annotation.*;

/**
 * Cache parameter annotation.
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheParam {

}
