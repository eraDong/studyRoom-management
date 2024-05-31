package com.yxy.studyroomserver.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Invalid parameter type");
        errorDetails.put("parameter", ex.getName());
        errorDetails.put("expectedType", ex.getRequiredType().getName());
        errorDetails.put("providedValue", ex.getValue().toString());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}

