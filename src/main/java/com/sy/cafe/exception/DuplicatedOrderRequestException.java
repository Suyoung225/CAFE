package com.sy.cafe.exception;

public class DuplicatedOrderRequestException extends RuntimeException{
    String message;

    public DuplicatedOrderRequestException(String message){
        super(message);
    }
}
