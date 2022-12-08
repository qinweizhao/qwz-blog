package com.qinweizhao.blog.exception;

/**
 * Exception caused by entity existence already.
 *
 * @author qinweizhao
 * @since 2022/3/15
 */
public class AlreadyExistsException extends BadRequestException {

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
