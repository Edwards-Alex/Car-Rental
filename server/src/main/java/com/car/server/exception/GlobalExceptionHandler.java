package com.car.server.exception;

import com.car.server.utility.ApiResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, Object> res = new HashMap<>();
        FieldError error = ex.getBindingResult().getFieldErrors().get(0);
        res.put("success", false);
        res.put("message", error.getDefaultMessage());
        return res;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    @ResponseBody
    public Map<String,Object> handleJwtExceptions(JwtException ex){
        Map<String, Object> res = new HashMap<>();
        res.put("success",false);
        res.put("message",ex.getMessage());
        return res;
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApiResponse<?> handleBusinessException(BusinessException ex) {
        return ApiResponse.error(ex.getSuccess(),ex.getErrorCode(),ex.getMessage());
    }
}

