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
        // Can thiệp vào tất cả các phản hồi
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

        // Chuyển đổi ServerHttpResponse thành HttpServletResponse để lấy status code
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        // Nếu response trả về kiểu String hoặc Resource, không cần thay đổi gì
        if (body instanceof String || body instanceof Resource) {
            return body;
        }

        // Nếu status >= 400, không thay đổi gì (trả về lỗi nguyên vẹn)
        if (status >= 400) {
            return body;
        } else {
            // Kiểm tra nếu body là kiểu ApiResponse rồi thì không cần bao bọc lại nữa
            if (body instanceof ApiResponse) {
                //không cần bọc thêm ApiResponse
                return body;
            }

            // Khởi tạo đối tượng ApiResponse để trả về
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setCode(1000);  // Mã code mặc định

            // Lấy annotation ApiMessage và thiết lập message
            ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
            String message = apiMessage != null ? apiMessage.value() : "CALL API SUCCESS";
            apiResponse.setMessage(message);

            // Thiết lập dữ liệu (body) trả về
            apiResponse.setResult(body);

            return apiResponse;
        }
    }
}
