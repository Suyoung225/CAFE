package com.sy.cafe.exception;

public class BadRequestException extends RuntimeException{
    String message;

    public BadRequestException(String message){
        super(message);
    }
}
