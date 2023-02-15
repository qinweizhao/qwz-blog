package com.qinweizhao.blog.exception;

/**
 * repeat type exception
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
public class RepeatTypeException extends ServiceException {
    public RepeatTypeException(String message) {
        super(message);
    }

    public RepeatTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
