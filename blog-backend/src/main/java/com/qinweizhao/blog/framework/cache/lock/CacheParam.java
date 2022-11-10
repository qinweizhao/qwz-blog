package com.qinweizhao.blog.framework.cache.lock;

import java.lang.annotation.*;

/**
 * Cache parameter annotation.
 *
 * @since 3/28/19
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheParam {

}
