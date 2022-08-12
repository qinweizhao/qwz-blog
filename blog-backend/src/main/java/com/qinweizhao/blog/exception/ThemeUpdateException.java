package com.qinweizhao.blog.exception;

/**
 * Theme update exception.
 *
 * @author johnniang
 * @since 19-5-30
 */
public class ThemeUpdateException extends ServiceException {

    public ThemeUpdateException(String message) {
        super(message);
    }

    public ThemeUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
