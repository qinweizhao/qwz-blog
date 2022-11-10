package com.qinweizhao.blog.exception;

/**
 * Missing property value exception.
 *
 * @since 3/22/19
 */
public class MissingPropertyException extends BadRequestException {

    public MissingPropertyException(String message) {
        super(message);
    }

    public MissingPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
