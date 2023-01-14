package com.sy.cafe.service;

import com.sy.cafe.domain.Point;
import com.sy.cafe.domain.PointType;
import com.sy.cafe.domain.User;
import com.sy.cafe.dto.response.PointResponseDto;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.repository.PointRepository;
import com.sy.cafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointService {
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    @Transactional
    public PointResponseDto chargePoint(Long id, Long chargeAmount) {
        // 입력 받은 id 존재여부
        User user = userRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_FOUND));
        // 포인트 충전
        Long pointAfterCharge = user.chargePoint(chargeAmount);
        // 포인트 충전 기록
        Point point = Point.builder()
                .type(PointType.CHARGE)
                .user(user)
                .point(chargeAmount)
                .build();
        pointRepository.save(point);

        return new PointResponseDto(id, pointAfterCharge);
    }


}
