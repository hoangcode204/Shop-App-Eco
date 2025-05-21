package com.example.ShopAppEcomere.util;

import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.validator.ApiMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        if (body instanceof String || body instanceof Resource) {
            return body;
        }

        if (status >= 400) {
            return body;
        }

        if (body instanceof ApiResponse apiResponse) {
            // Nếu message chưa có → tự set message từ @ApiMessage
            if (apiResponse.getMessage() == null || apiResponse.getMessage().isEmpty()) {
                ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
                String message = apiMessage != null ? apiMessage.value() : "CALL API SUCCESS";
                apiResponse.setMessage(message);
            }
            return apiResponse;
        }

        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);

        ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
        String message = apiMessage != null ? apiMessage.value() : "CALL API SUCCESS";
        apiResponse.setMessage(message);

        apiResponse.setResult(body);

        return apiResponse;
    }
}

