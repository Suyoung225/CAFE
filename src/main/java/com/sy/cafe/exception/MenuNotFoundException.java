package com.sy.cafe.exception;

import java.util.NoSuchElementException;

public class MenuNotFoundException extends NoSuchElementException {
    String message;

    public MenuNotFoundException(String message){
        super(message);
    }
}
