package com.example.test.utils;

import org.springframework.http.MediaType;

import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.test.core.Response;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FormatResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // Kiểm tra nếu yêu cầu là từ Actuator để tránh format response
        String path = request.getURI().getPath();
        if (path.startsWith("/actuator")) {
            return body;
        }

        HttpServletResponse servletResponse = ((org.springframework.http.server.ServletServerHttpResponse) response)
                .getServletResponse();
        int status = servletResponse.getStatus();
        Response<Object> responseObj = new Response<Object>();
        if (status >= 400) {
            return body;

        } else {
            responseObj.setMessage("success");
            responseObj.setStatus(status);
            responseObj.setData(body);
        }

        return responseObj;
    }

}
