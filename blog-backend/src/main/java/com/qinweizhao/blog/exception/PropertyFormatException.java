package com.qinweizhao.blog.exception;

/**
 * Property format exception.
 *
 * @author johnniang
 * @since 3/27/19
 */
public class PropertyFormatException extends BadRequestException {

    public PropertyFormatException(String message) {
        super(message);
    }

    public PropertyFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
