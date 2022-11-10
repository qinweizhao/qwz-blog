package com.qinweizhao.blog.exception;

/**
 * File operation exception.
 *
 * @since 3/27/19
 */
public class FileOperationException extends ServiceException {
    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
