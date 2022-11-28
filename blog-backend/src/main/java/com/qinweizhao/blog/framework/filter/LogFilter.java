package com.qinweizhao.blog.framework.filter;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for logging.
 * @author qinweizhao
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 9)
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String remoteAddr = ServletUtil.getClientIP(request);

        log.debug("");
        log.debug("请求路径: [{}], 方法: [{}], IP: [{}]", request.getRequestURL(), request.getMethod(), remoteAddr);

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        log.debug("请求路径: [{}], 方法: [{}], IP: [{}], 状态: [{}], 耗时: [{}] ms", request.getRequestURL(), request.getMethod(), remoteAddr, response.getStatus(), System.currentTimeMillis() - startTime);
        log.debug("");
    }
}
