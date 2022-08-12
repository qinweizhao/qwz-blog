package com.qinweizhao.blog.exception;

/**
 * Theme property missing exception.
 *
 * @author johnniang
 * @since 4/11/19
 */
public class ThemePropertyMissingException extends BadRequestException {

    public ThemePropertyMissingException(String message) {
        super(message);
    }

    public ThemePropertyMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
