package com.sy.cafe.exception;

public class BalanceInsufficientException extends RuntimeException{
    String message;

    public BalanceInsufficientException(String message){
        super(message);
    }
}
