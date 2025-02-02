package com.example.test.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.test.core.error.BadRequestException;
import com.example.test.core.error.ForbiddenException;
import com.example.test.core.error.UnauthorizedException;

import java.util.List;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Response<Object>> handleException(Exception e) {
        Response<Object> response = new Response<>();
        response.setMessage("error");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setError("Internal server error");

        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {
            BadRequestException.class,
            UsernameNotFoundException.class,
            UnauthorizedException.class,
            BadCredentialsException.class,
            ForbiddenException.class
    })
    public ResponseEntity<Response<Object>> handleAllException(Exception e) {
        Response<Object> response = new Response<>();
        response.setMessage("error");
        response.setStatus(getStatusFromException(e).value());
        response.setError(e.getMessage());
        return new ResponseEntity<>(response, getStatusFromException(e));
    }

    private HttpStatus getStatusFromException(Exception e) {
        if (e instanceof BadRequestException) {
            return HttpStatus.BAD_REQUEST;
        } else if (e instanceof UsernameNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (e instanceof UnauthorizedException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (e instanceof BadCredentialsException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (e instanceof ForbiddenException) {
            return HttpStatus.FORBIDDEN;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Response<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        Response<Object> response = new Response<>();
        response.setMessage("error");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(e.getBody().getDetail());
        List<String> errors = fieldErrors.stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
        response.setError(errors.size() > 1 ? errors : errors.get(0));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {

        String message = "Dữ liệu không hợp lệ";
        Response<?> response = new Response<>();
        response.setError(message);
        response.setMessage("error");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Response<Object>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        Response<Object> response = new Response<>();
        response.setMessage("Endpoint not found");
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
