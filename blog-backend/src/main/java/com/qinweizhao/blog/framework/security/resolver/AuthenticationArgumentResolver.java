package com.qinweizhao.blog.framework.security.resolver;

import com.qinweizhao.blog.exception.AuthenticationException;
import com.qinweizhao.blog.framework.security.authentication.Authentication;
import com.qinweizhao.blog.framework.security.context.SecurityContextHolder;
import com.qinweizhao.blog.framework.security.support.UserDetail;
import com.qinweizhao.blog.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * todo remove
 * Authentication argument resolver.
 *
 * @author qinweizhao
 * @since 2019-03-19
 */
@Slf4j
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    public AuthenticationArgumentResolver() {
        log.debug("Initializing AuthenticationArgumentResolver");
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return Authentication.class.isAssignableFrom(parameterType)
                || UserDetail.class.isAssignableFrom(parameterType)
                || User.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {
        log.debug("Handle AuthenticationArgument");

        Class<?> parameterType = parameter.getParameterType();

        Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new AuthenticationException("You haven't signed in yet"));

        if (Authentication.class.isAssignableFrom(parameterType)) {
            return authentication;
        } else if (UserDetail.class.isAssignableFrom(parameterType)) {
            return authentication.getDetail();
        } else if (User.class.isAssignableFrom(parameterType)) {
            return authentication.getDetail().getUser();
        }

        // Should never happen...
        throw new UnsupportedOperationException("Unknown parameter type: " + parameterType);
    }

}
