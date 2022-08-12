package com.qinweizhao.blog.exception;

/**
 * Email exception.
 *
 * @author johnniang
 * @since 19-4-23
 */
public class EmailException extends ServiceException {

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
