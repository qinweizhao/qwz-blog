package com.qinweizhao.blog.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception of entity not found.
 * @author qinweizhao
 * @since 2019-03-17
 */
public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
