package com.sy.cafe.controller;

import com.sy.cafe.dto.request.UserRegisterDto;
import com.sy.cafe.dto.response.UserResponseDto;
import com.sy.cafe.global.ResponseDto;
import com.sy.cafe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseDto<UserResponseDto> register(@RequestBody UserRegisterDto dto){
        return ResponseDto.success(userService.register(dto.getNickname()));
    }
}
