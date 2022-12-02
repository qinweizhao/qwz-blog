package com.qinweizhao.blog.framework.security.handler;

import com.qinweizhao.blog.exception.BaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author qinweizhao
 * @since 2019-03-19
 */
public interface AuthenticationFailureHandler {

    /**
     * 当用户身份验证未成功时调用
     *
     * @param request   request
     * @param response  response
     * @param exception exception
     * @throws IOException      io exception
     * @throws ServletException service exception
     */
    void onFailure(HttpServletRequest request, HttpServletResponse response, BaseException exception) throws IOException, ServletException;
}
