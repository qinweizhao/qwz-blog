package com.qinweizhao.blog.exception;

/**
 * Theme not support exception.
 *
 * @author ryanwang
 * @since 2020-02-03
 */
public class ThemeNotSupportException extends BadRequestException {

    public ThemeNotSupportException(String message) {
        super(message);
    }

    public ThemeNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }
}
