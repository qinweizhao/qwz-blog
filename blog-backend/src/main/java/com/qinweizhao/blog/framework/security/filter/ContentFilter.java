package com.qinweizhao.blog.framework.security.filter;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.security.handler.ContentAuthenticationFailureHandler;
import com.qinweizhao.blog.framework.security.service.OneTimeTokenService;
import com.qinweizhao.blog.util.HaloUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Content filter
 *
 * @since 19-5-6
 */
@Component
@Order(-1)
public class ContentFilter extends AbstractAuthenticationFilter {

    public ContentFilter(MyBlogProperties myBlogProperties,
                         AbstractStringCacheStore cacheStore,
                         OneTimeTokenService oneTimeTokenService) {
        super(myBlogProperties, cacheStore, oneTimeTokenService);

        addUrlPatterns("/**");

        String adminPattern = HaloUtils.ensureBoth(myBlogProperties.getAdminPath(), "/") + "**";
        addExcludeUrlPatterns(
                adminPattern,
                "/api/**",
                "/install",
                "/version",
                "/js/**",
                "/css/**");

        // set failure handler
        setFailureHandler(new ContentAuthenticationFailureHandler());
    }

    @Override
    protected String getTokenFromRequest(HttpServletRequest request) {
        return null;
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Do nothing
        filterChain.doFilter(request, response);
    }
}
