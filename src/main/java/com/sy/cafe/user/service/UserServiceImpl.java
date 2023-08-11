package com.sy.cafe.user.service;

import com.sy.cafe.aop.DistributeLock;
import com.sy.cafe.exception.UserNotFoundException;
import com.sy.cafe.pointhistory.service.PointHistoryService;
import com.sy.cafe.user.controller.dto.PointChargeRequestDto;
import com.sy.cafe.user.domain.User;
import com.sy.cafe.user.controller.dto.UserPointDto;
import com.sy.cafe.user.controller.dto.UserDto;
import com.sy.cafe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PointHistoryService pointHistoryService;

    @Override
    @Transactional
    public UserDto registerUser(String nickname) {
        User user = new User(nickname);
        userRepository.save(user);
        return new UserDto(user);
    }

    @Override
    @DistributeLock(key = "userId")
    public UserPointDto chargePoint(PointChargeRequestDto chargeRequestDto){
        // 입력 받은 id 존재 여부
        User user = getUser(chargeRequestDto.getUserId());
        // 포인트 충전
        Long pointAfterCharge = user.chargePoint(chargeRequestDto.getPoint());
        // 포인트 기록
        pointHistoryService.chargePoint(chargeRequestDto.getPoint(), user);
        return new UserPointDto(chargeRequestDto.getUserId(), pointAfterCharge);
    }

    // 주문 가능 여부 확인 & 결제
    @Override
    @Transactional
    public long order(Long id, Long totalAmount){
        User user = getUser(id);
        long point = user.usePoint(totalAmount);
        pointHistoryService.usePoint(totalAmount, user);
        return point;
    }

    private User getUser(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("해당 id의 유저가 존재하지 않습니다."));
    }

}
