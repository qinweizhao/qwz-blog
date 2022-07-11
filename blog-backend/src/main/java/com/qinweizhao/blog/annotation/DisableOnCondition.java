package com.qinweizhao.blog.annotation;

import com.qinweizhao.blog.model.enums.Mode;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 该注解可以限制某些条件下禁止访问api
 *
 * @author guqing
 * @date 2020-02-14 13:48
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisableOnCondition {
    @AliasFor("mode")
    Mode value() default Mode.DEMO;

    @AliasFor("value")
    Mode mode() default Mode.DEMO;
}
