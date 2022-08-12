package com.qinweizhao.blog.exception;

/**
 * repeat type exception
 *
 * @author bestsort
 * @since 3/13/20 5:03 PM
 */
public class RepeatTypeException extends ServiceException {
    public RepeatTypeException(String message) {
        super(message);
    }

    public RepeatTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
