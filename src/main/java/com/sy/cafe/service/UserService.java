package com.sy.cafe.service;

import com.sy.cafe.aop.DistributeLock;
import com.sy.cafe.domain.User;
import com.sy.cafe.dto.UserDto;
import com.sy.cafe.dto.response.PointResponseDto;
import com.sy.cafe.dto.response.UserResponseDto;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PointService pointService;

    @Transactional
    public UserResponseDto register(String nickname) {
        User user = new User(nickname);
        userRepository.save(user);
        return new UserResponseDto(user);
    }

    @DistributeLock(key = "id")
    public PointResponseDto chargePoint(Long id, Long chargeAmount){
        // 입력 받은 id 존재여부
        User user = getUser(id);
        // 포인트 충전
        Long pointAfterCharge = user.chargePoint(chargeAmount);
        // 포인트 기록
        pointService.chargePoint(chargeAmount, new UserDto(user));
        return new PointResponseDto(id, pointAfterCharge);
    }

    // 주문 가능 여부 & 결제
    @DistributeLock(key = "id")
    public long order(Long id, Long totalAmount){
        User user = getUser(id);
        if(totalAmount > user.getPoint())
            throw new RequestException(ErrorCode.BALANCE_INSUFFICIENT);
        long point = user.usePoint(totalAmount);
        pointService.usePoint(totalAmount, new UserDto(user));

        return point;

    }

    private User getUser(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_FOUND));
    }

}
