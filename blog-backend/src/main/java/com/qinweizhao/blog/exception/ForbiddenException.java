package com.qinweizhao.blog.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception caused by accessing forbidden resources.
 * @author qinweizhao
 * @since 2019-03-17
 */
public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
