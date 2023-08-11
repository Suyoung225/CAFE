package com.sy.cafe.exception;

public class DuplicatedMenuException extends IllegalArgumentException{
    String message;

    public DuplicatedMenuException(String message){
        super(message);
    }
}
