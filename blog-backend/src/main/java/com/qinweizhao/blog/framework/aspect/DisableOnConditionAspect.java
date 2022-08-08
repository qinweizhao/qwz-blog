package com.qinweizhao.blog.framework.aspect;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.config.properties.HaloProperties;
import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.model.enums.Mode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 自定义注解DisableApi的切面
 *
 * @author guqing
 * @date 2020-02-14 14:08
 */
@Aspect
@Slf4j
@Component
public class DisableOnConditionAspect {

    private final HaloProperties haloProperties;

    public DisableOnConditionAspect(HaloProperties haloProperties) {
        this.haloProperties = haloProperties;
    }

    @Pointcut("@annotation(com.qinweizhao.blog.framework.annotation.DisableOnCondition)")
    public void pointcut() {
    }

    @Around("pointcut() && @annotation(disableApi)")
    public Object around(ProceedingJoinPoint joinPoint,
                         DisableOnCondition disableApi) throws Throwable {
        Mode mode = disableApi.mode();
        if (haloProperties.getMode().equals(mode)) {
            throw new ForbiddenException("禁止访问");
        }

        return joinPoint.proceed();
    }
}
