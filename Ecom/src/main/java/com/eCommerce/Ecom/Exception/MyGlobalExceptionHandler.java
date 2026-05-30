package com.eCommerce.Ecom.Exception;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> MyCusMetArgNotValEx (MethodArgumentNotValidException exp) {
        Map<String,String> stringMap = new HashMap<>();
        exp.getBindingResult().getAllErrors().forEach(
                objectError -> {
                    String filedName = objectError.getObjectName();
                    String message = objectError.getDefaultMessage();
                    stringMap.put(filedName, message);
                }
        );
        return new ResponseEntity<>(stringMap,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> MyResNotFouEx(ResourceNotFoundException exception) {
        String message = exception.getMessage();
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }
}
