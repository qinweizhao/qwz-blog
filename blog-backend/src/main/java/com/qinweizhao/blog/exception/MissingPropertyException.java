package com.qinweizhao.blog.exception;

/**
 * Missing property value exception.
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
public class MissingPropertyException extends BadRequestException {

    public MissingPropertyException(String message) {
        super(message);
    }

    public MissingPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
