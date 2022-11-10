package com.qinweizhao.blog.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception caused by bad request.
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
