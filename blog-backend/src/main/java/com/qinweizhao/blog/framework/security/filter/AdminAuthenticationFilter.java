package com.qinweizhao.blog.framework.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.AuthenticationException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.security.authentication.AuthenticationImpl;
import com.qinweizhao.blog.framework.security.context.SecurityContextHolder;
import com.qinweizhao.blog.framework.security.context.SecurityContextImpl;
import com.qinweizhao.blog.framework.security.handler.DefaultAuthenticationFailureHandler;
import com.qinweizhao.blog.framework.security.service.OneTimeTokenService;
import com.qinweizhao.blog.framework.security.support.UserDetail;
import com.qinweizhao.blog.framework.security.util.AuthUtils;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.qinweizhao.blog.model.support.BlogConst.ADMIN_TOKEN_HEADER_NAME;
import static com.qinweizhao.blog.model.support.BlogConst.ADMIN_TOKEN_QUERY_NAME;

/**
 * Admin authentication filter.
 */
@Slf4j
@Component
@Order(1)
public class AdminAuthenticationFilter extends AbstractAuthenticationFilter {

    private final MyBlogProperties myBlogProperties;

    private final UserService userService;

    public AdminAuthenticationFilter(AbstractStringCacheStore cacheStore,
                                     UserService userService,
                                     MyBlogProperties myBlogProperties,
                                     ConfigService configService,
                                     OneTimeTokenService oneTimeTokenService,
                                     ObjectMapper objectMapper) {
        super(myBlogProperties, cacheStore, oneTimeTokenService);
        this.userService = userService;
        this.myBlogProperties = myBlogProperties;

        addUrlPatterns("/api/admin/**", "/api/content/comments");

        // todo
        // "/api/admin/login",
        addExcludeUrlPatterns(
                "/api/admin/login",
                "/api/admin/refresh/*",
                "/api/admin/installations",
                "/api/admin/migrations/halo",
                "/api/admin/password/code",
                "/api/admin/password/reset",
                "/api/admin/login/precheck"
        );

        // set failure handler
        DefaultAuthenticationFailureHandler failureHandler = new DefaultAuthenticationFailureHandler();
        failureHandler.setProductionEnv(myBlogProperties.isProductionEnv());
        failureHandler.setObjectMapper(objectMapper);

        setFailureHandler(failureHandler);

    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!myBlogProperties.isAuthEnabled()) {
            // Set security
            userService.getCurrentUser().ifPresent(user ->
                    SecurityContextHolder.setContext(new SecurityContextImpl(new AuthenticationImpl(new UserDetail(user)))));

            // Do filter
            filterChain.doFilter(request, response);
            return;
        }

        // Get token from request
        String token = getTokenFromRequest(request);

        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("未登录，请登录后访问");
        }

        // Get user id from cache
        Optional<Integer> optionalUserId = cacheStore.getAny(AuthUtils.buildTokenAccessKey(token), Integer.class);

        if (!optionalUserId.isPresent()) {
            throw new AuthenticationException("Token 已过期或不存在").setErrorData(token);
        }

        // Get the user
        User user = userService.getById(optionalUserId.get());

        // Build user detail
        UserDetail userDetail = new UserDetail(user);

        // Set security
        SecurityContextHolder.setContext(new SecurityContextImpl(new AuthenticationImpl(userDetail)));

        // Do filter
        filterChain.doFilter(request, response);
    }

    @Override
    protected String getTokenFromRequest(@NonNull HttpServletRequest request) {
        return getTokenFromRequest(request, ADMIN_TOKEN_QUERY_NAME, ADMIN_TOKEN_HEADER_NAME);
    }

}
