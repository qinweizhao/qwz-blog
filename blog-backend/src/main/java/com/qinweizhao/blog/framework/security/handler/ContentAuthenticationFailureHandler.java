package com.qinweizhao.blog.framework.security.handler;

import com.qinweizhao.blog.exception.BaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 内容认证失败处理程序
 *
 * @since 19-5-6
 */
public class ContentAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, BaseException exception) throws IOException, ServletException {

        // Forward to error
        request.getRequestDispatcher(request.getContextPath() + "/error").forward(request, response);
    }
}
