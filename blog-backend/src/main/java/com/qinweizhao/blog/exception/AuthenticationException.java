package com.qinweizhao.blog.exception;

import org.springframework.http.HttpStatus;

/**
 * Authentication exception.
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
public class AuthenticationException extends BaseException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
