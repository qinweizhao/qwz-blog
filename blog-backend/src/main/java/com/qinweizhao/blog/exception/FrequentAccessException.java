package com.qinweizhao.blog.exception;

/**
 * Frequent access exception.
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
public class FrequentAccessException extends BadRequestException {

    public FrequentAccessException(String message) {
        super(message);
    }

    public FrequentAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
