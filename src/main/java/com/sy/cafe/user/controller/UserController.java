package com.sy.cafe.user.controller;

import com.sy.cafe.user.controller.dto.PointChargeRequestDto;
import com.sy.cafe.user.controller.dto.UserPointDto;
import com.sy.cafe.global.ResponseDto;
import com.sy.cafe.user.controller.dto.UserDto;
import com.sy.cafe.user.controller.dto.UserRegisterDto;
import com.sy.cafe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegisterDto dto){
        return ResponseEntity.ok(userService.registerUser(dto.getNickname()));
    }

    @PostMapping("/point/charge")
    public ResponseEntity<UserPointDto> chargePoint(@RequestBody PointChargeRequestDto dto){
        return ResponseEntity.ok(userService.chargePoint(dto));
    }

}
