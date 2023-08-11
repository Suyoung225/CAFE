package com.sy.cafe.user.service;

import com.sy.cafe.user.controller.dto.UserPointDto;
import com.sy.cafe.user.controller.dto.PointChargeRequestDto;
import com.sy.cafe.user.controller.dto.UserDto;

public interface UserService {
    UserDto registerUser(String nickname);
    UserPointDto chargePoint(PointChargeRequestDto pointChargeRequestDto);
    long order(Long id, Long totalAmount);
}
