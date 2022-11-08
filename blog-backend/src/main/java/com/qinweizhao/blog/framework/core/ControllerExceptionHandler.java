package com.qinweizhao.blog.framework.core;

import com.qinweizhao.blog.exception.BaseException;
import com.qinweizhao.blog.exception.EmailException;
import com.qinweizhao.blog.model.support.BaseResponse;
import com.qinweizhao.blog.util.ExceptionUtils;
import com.qinweizhao.blog.util.ValidationUtils;
import com.sun.mail.util.MailConnectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Exception handler of controller.
 *
 * @author johnniang
 */
@RestControllerAdvice(value = {"com.qinweizhao.blog.controller.admin.api", "com.qinweizhao.blog.controller.content.api"})
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleDataIntegrityViolationException(SQLException e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        baseResponse.setMessage("SQL 异常");
        return baseResponse;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        baseResponse.setMessage(String.format("请求字段缺失, 类型为 %s，名称为 %s", e.getParameterType(), e.getParameterName()));
        return baseResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleConstraintViolationException(ConstraintViolationException e) {
        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponse.setMessage("字段验证错误，请完善后重试！");
        baseResponse.setData(ValidationUtils.mapWithValidError(e.getConstraintViolations()));
        return baseResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponse.setMessage("字段验证错误，请完善后重试！");
        Map<String, String> errMap = ValidationUtils.mapWithFieldError(e.getBindingResult().getFieldErrors());
        baseResponse.setData(errMap);
        return baseResponse;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return baseResponse;
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public BaseResponse<?> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        return baseResponse;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponse.setMessage("缺失请求主体");
        return baseResponse;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public BaseResponse<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        baseResponse.setStatus(status.value());
        baseResponse.setMessage(status.getReasonPhrase());
        return baseResponse;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleUploadSizeExceededException(MaxUploadSizeExceededException e) {
        BaseResponse<Object> response = handleBaseException(e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage("当前请求超出最大限制：" + e.getMaxUploadSize() + " bytes");
        return response;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<?>> handleHaloException(BaseException e) {
        BaseResponse<Object> baseResponse = handleBaseException(e);
        baseResponse.setStatus(e.getStatus().value());
        baseResponse.setData(e.getErrorData());
        return new ResponseEntity<>(baseResponse, e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleGlobalException(Exception e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        baseResponse.setStatus(status.value());
        baseResponse.setMessage(status.getReasonPhrase());
        return baseResponse;
    }

    @ExceptionHandler(EmailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleMessagingException(MessagingException e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        String message;
        if (e instanceof MailConnectException) {
            if (e.getCause() instanceof UnknownHostException) {
                message = "SMTP 服务器解析错误，请检查 SMTP 服务器地址";
            } else if (e.getCause() instanceof ConnectException) {
                message = "无法连接至邮件服务器，请检查地址和端口号";
            } else if (e.getCause() instanceof SocketException) {
                message = "网络连接超时，请检查网络连通性";
            } else {
                message = "无法连接至邮件服务器，请检查地址和端口号";
            }
        } else if (e instanceof NoSuchProviderException) {
            message = "发送协议配置错误，请检查发送协议";
        } else if (e instanceof AuthenticationFailedException) {
            message = "邮箱账号密码验证失败，请检查密码是否应为授权码";
        } else {
            message = "出现未知错误，请检查系统日志";
        }
        baseResponse.setMessage(message);
        return baseResponse;
    }

    private <T> BaseResponse<T> handleBaseException(Throwable t) {
        Assert.notNull(t, "Throwable must not be null");

        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setMessage(t.getMessage());

        log.error("Captured an exception:", t);

        if (log.isDebugEnabled()) {
            baseResponse.setDevMessage(ExceptionUtils.getStackTrace(t));
        }

        return baseResponse;
    }
}