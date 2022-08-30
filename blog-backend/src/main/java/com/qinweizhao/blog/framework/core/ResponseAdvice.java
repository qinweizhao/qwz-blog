package com.qinweizhao.blog.framework.core;

import com.qinweizhao.blog.model.support.BaseResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一封装返回
 *
 * @author qinweizhao
 * @since 2022/7/31
 */
@ControllerAdvice("com.qinweizhao.blog.controller")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {


    /**
     * 判断是否要交给 beforeBodyWrite 方法执行，ture：需要；false：不需要
     *
     * @param returnType    returnType
     * @param converterType converterType
     * @return boolean
     */
    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    @NonNull
    public final Object beforeBodyWrite(@Nullable Object body,
                                        @NotNull MethodParameter returnType,
                                        @NotNull MediaType contentType,
                                        @NotNull Class<? extends HttpMessageConverter<?>> converterType,
                                        @NotNull ServerHttpRequest request,
                                        @NotNull ServerHttpResponse response) {
        MappingJacksonValue container = getOrCreateContainer(body);

        beforeBodyWriteInternal(container, response);
        return container;
    }

    /**
     * 将主体包装在 {@link MappingJacksonValue} 值容器中（用于提供额外的序列化指令），或者如果已经包装，则直接转换它。
     */
    private MappingJacksonValue getOrCreateContainer(Object body) {
        return body instanceof MappingJacksonValue ? (MappingJacksonValue) body : new MappingJacksonValue(body);
    }

    /**
     * 在正文写入内部之前
     *
     * @param bodyContainer bodyContainer
     * @param response      response
     */
    private void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, ServerHttpResponse response) {
        Object returnBody = bodyContainer.getValue();

        // 如果已经经过包装则不处理
        if (returnBody instanceof BaseResponse) {
            BaseResponse<?> baseResponse = (BaseResponse<?>) returnBody;
            Integer status = baseResponse.getStatus();
            if (!ObjectUtils.isEmpty(status) && !ObjectUtils.isEmpty(HttpStatus.resolve(status))) {
                HttpStatus httpStatus = HttpStatus.resolve(status);
                assert httpStatus != null;
                response.setStatusCode(httpStatus);
            }
            return;
        }

        // 包装返回体
        BaseResponse<?> baseResponse = BaseResponse.ok(returnBody);
        bodyContainer.setValue(baseResponse);
        response.setStatusCode(HttpStatus.valueOf(baseResponse.getStatus()));
    }

}
