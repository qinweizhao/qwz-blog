package com.qinweizhao.blog.framework.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.AuthenticationException;
import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.model.properties.ApiProperties;
import com.qinweizhao.blog.model.properties.CommentProperties;
import com.qinweizhao.blog.framework.security.handler.DefaultAuthenticationFailureHandler;
import com.qinweizhao.blog.framework.security.service.OneTimeTokenService;
import com.qinweizhao.blog.service.ConfigService;
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

import static com.qinweizhao.blog.model.support.HaloConst.API_ACCESS_KEY_HEADER_NAME;
import static com.qinweizhao.blog.model.support.HaloConst.API_ACCESS_KEY_QUERY_NAME;

/**
 * Api authentication Filter
 *
 * @author johnniang
 */
@Slf4j
@Component
@Order(0)
public class ApiAuthenticationFilter extends AbstractAuthenticationFilter {

    private final ConfigService configService;

    public ApiAuthenticationFilter(MyBlogProperties myBlogProperties,
                                   ConfigService configService,
                                   AbstractStringCacheStore cacheStore,
                                   OneTimeTokenService oneTimeTokenService,
                                   ObjectMapper objectMapper) {
        super(myBlogProperties, cacheStore, oneTimeTokenService);
        this.configService = configService;

        addUrlPatterns("/api/content/**");

        addExcludeUrlPatterns(
                "/api/content/**/comments",
                "/api/content/**/comments/**",
                "/api/content/options/comment"
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
            filterChain.doFilter(request, response);
            return;
        }

        // Get api_enable from option
        Boolean apiEnabled = configService.getByPropertyOrDefault(ApiProperties.API_ENABLED, Boolean.class, false);

        if (!apiEnabled) {
            throw new ForbiddenException("API has been disabled by blogger currently");
        }

        // Get access key
        String accessKey = getTokenFromRequest(request);

        if (StringUtils.isBlank(accessKey)) {
            // If the access key is missing
            throw new AuthenticationException("Missing API access key");
        }

        // Get access key from option
        Optional<String> optionalAccessKey = configService.getByProperty(ApiProperties.API_ACCESS_KEY, String.class);

        if (!optionalAccessKey.isPresent()) {
            // If the access key is not set
            throw new AuthenticationException("API access key hasn't been set by blogger");
        }

        if (!StringUtils.equals(accessKey, optionalAccessKey.get())) {
            // If the access key is mismatch
            throw new AuthenticationException("API access key is mismatch").setErrorData(accessKey);
        }

        // Do filter
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean result = super.shouldNotFilter(request);

        if (antPathMatcher.match("/api/content/*/comments", request.getServletPath())) {
            Boolean commentApiEnabled = configService.getByPropertyOrDefault(CommentProperties.API_ENABLED, Boolean.class, true);
            if (!commentApiEnabled) {
                // If the comment api is disabled
                result = false;
            }
        }
        return result;
    }

    @Override
    protected String getTokenFromRequest(@NonNull HttpServletRequest request) {
        return getTokenFromRequest(request, API_ACCESS_KEY_QUERY_NAME, API_ACCESS_KEY_HEADER_NAME);
    }
}
