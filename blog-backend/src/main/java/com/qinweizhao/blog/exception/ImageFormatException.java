package com.qinweizhao.blog.exception;

/**
 * Image format exception.
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
public class ImageFormatException extends BadRequestException {

    public ImageFormatException(String message) {
        super(message);
    }

    public ImageFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
