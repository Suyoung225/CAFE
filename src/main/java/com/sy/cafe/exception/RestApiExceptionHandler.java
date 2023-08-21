package com.sy.cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { DuplicatedMenuException.class, DuplicatedOrderRequestException.class, BadRequestException.class, BalanceInsufficientException.class })
    public ResponseEntity<Map<String, String>> handleApiRequestException(RuntimeException e) {
        return ResponseEntity.badRequest().body(getMap(e.getMessage()));
    }

    @ExceptionHandler(value = { UserNotFoundException.class, MenuNotFoundException.class,  })
    public ResponseEntity<Map<String, String>> handleApiRequestException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMap(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e){
        Map<String, String> map = new HashMap<>();
        String field = e.getBindingResult().getFieldErrors().get(0).getField();
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        map.put("field", field);
        map.put("message", message);

        return ResponseEntity.badRequest().body(map);
    }

    Map<String, String> getMap(String msg){
        Map<String, String> map = new HashMap<>();
        map.put("message", msg);
        return map;
    }

}
