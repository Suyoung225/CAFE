package com.sy.cafe.exception;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
    String message;

    public UserNotFoundException(String message){
        super(message);
    }
}
