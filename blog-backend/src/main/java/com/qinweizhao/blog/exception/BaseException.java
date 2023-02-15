package com.qinweizhao.blog.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception of the project.
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
public abstract class BaseException extends RuntimeException {

    /**
     * Error errorData.
     */
    private Object errorData;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Http status code
     *
     * @return {@link HttpStatus}
     */

    public abstract HttpStatus getStatus();


    public Object getErrorData() {
        return errorData;
    }

    /**
     * Sets error errorData.
     *
     * @param errorData error data
     * @return current exception.
     */

    public BaseException setErrorData(Object errorData) {
        this.errorData = errorData;
        return this;
    }
}
