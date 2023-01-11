package com.sy.cafe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),
    ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 데이터입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
