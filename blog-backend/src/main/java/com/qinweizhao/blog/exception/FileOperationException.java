package com.qinweizhao.blog.exception;

/**
 * File operation exception.
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
public class FileOperationException extends ServiceException {
    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
