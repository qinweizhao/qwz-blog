package com.qinweizhao.blog.framework.aspect;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.model.enums.Mode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author qinweizhao
 * @since 2022/9/27
 */
@Aspect
@Component
public class DisableOnConditionAspect {

    private final MyBlogProperties myBlogProperties;

    public DisableOnConditionAspect(MyBlogProperties myBlogProperties) {
        this.myBlogProperties = myBlogProperties;
    }

    @Around("@annotation(disableApi)")
    public Object around(ProceedingJoinPoint joinPoint, DisableOnCondition disableApi) throws Throwable {
        Mode mode = disableApi.mode();
        if (myBlogProperties.getMode().equals(mode)) {
            throw new ForbiddenException("禁止访问");
        }

        return joinPoint.proceed();
    }
}
