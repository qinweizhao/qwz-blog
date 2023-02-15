package com.qinweizhao.blog.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception caused by service.
 * @author qinweizhao
 * @since 2019-03-17
 */
public class ServiceException extends BaseException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
