package com.qinweizhao.blog.security.handler;

import com.qinweizhao.blog.exception.AbstractHaloException;
import com.qinweizhao.blog.exception.NotInstallException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 内容认证失败处理程序
 *
 * @author johnniang
 * @date 19-5-6
 */
public class ContentAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, AbstractHaloException exception) throws IOException, ServletException {
        if (exception instanceof NotInstallException) {
            response.sendRedirect(request.getContextPath() + "/install");
            return;
        }

        // Forward to error
        request.getRequestDispatcher(request.getContextPath() + "/error").forward(request, response);
    }
}
