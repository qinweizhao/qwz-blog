package com.qinweizhao.blog.controller.error;

import com.qinweizhao.blog.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.NestedServletException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 错误处理
 *
 * @author johnniang
 * @author qinweizhao
 * @since 2022/09/15
 */
@Component
@Slf4j
public class DefaultErrorController extends BasicErrorController {

    public DefaultErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
    }

    @Override
    protected HttpStatus getStatus(HttpServletRequest request) {
        HttpStatus status = super.getStatus(request);

        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (exception instanceof NestedServletException) {
            NestedServletException nse = (NestedServletException) exception;
            if (nse.getCause() instanceof BaseException) {
                status = resolveBlogException((BaseException) nse.getCause(), request);
            }
        } else if (exception instanceof BaseException) {
            status = resolveBlogException((BaseException) exception, request);
        }
        return status;
    }

    private HttpStatus resolveBlogException(BaseException baseException, HttpServletRequest request) {
        HttpStatus status = baseException.getStatus();
        if (log.isDebugEnabled()) {
            log.error("发生了异常", baseException);
        }
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status.value());
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, baseException);
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, baseException.getMessage());
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE, baseException.getClass());
        return status;
    }
}
