package com.example.test.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.test.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    // convert object to json
    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);

        response.setContentType("application/json;charset=UTF-8");

        Response<Object> res = new Response<>();

        res.setStatus(HttpStatus.UNAUTHORIZED.value());

        String errMessage = Optional.ofNullable(authException.getCause()).map(Throwable::getMessage)
                .orElse(authException.getMessage());

        res.setMessage(errMessage);
        res.setError("Unauthorized!");
        this.mapper.writeValue(response.getWriter(), res);
    }
}
