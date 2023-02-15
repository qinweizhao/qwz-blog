package com.qinweizhao.blog.exception;

/**
 * Email exception.
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
public class EmailException extends ServiceException {

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
