package com.qinweizhao.blog.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception caused by bad request.
 * @author qinweizhao
 * @since 2019-03-17
 */
public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
