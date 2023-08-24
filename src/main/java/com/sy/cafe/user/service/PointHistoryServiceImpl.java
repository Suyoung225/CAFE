package com.sy.cafe.user.service;

import com.sy.cafe.exception.UserNotFoundException;
import com.sy.cafe.user.controller.dto.PointHistoryResponseDto;
import com.sy.cafe.user.domain.PointHistory;
import com.sy.cafe.user.domain.PointType;
import com.sy.cafe.user.domain.User;
import com.sy.cafe.user.repository.PointRepository;
import com.sy.cafe.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointHistoryServiceImpl implements PointHistoryService{
    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public PointHistoryServiceImpl(PointRepository pointRepository, UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void chargePoint(Long chargeAmount, User user) {
        // 포인트 충전 이력 추가
        PointHistory pointHistory = PointHistory.builder()
                .type(PointType.CHARGE)
                .user(user)
                .point(chargeAmount)
                .build();
        pointRepository.save(pointHistory);

    }

    @Override
    @Transactional
    public void usePoint(Long orderAmount, User user){
        // 포인트 사용 이력 추가
        PointHistory pointHistory = PointHistory.builder()
                .type(PointType.PAYMENT)
                .user(user)
                .point(orderAmount)
                .build();
        pointRepository.save(pointHistory);
    }

    @Override
    public Page<PointHistoryResponseDto> showPointHistoryByType(Long userId, String type, Pageable pageable) {
        User user = getUser(userId);
        if(type != null && (type.equals("CHARGE") || type.equals("PAYMENT"))){
            PointType pointType = PointType.valueOf(type);
            return pointRepository.findByUserAndTypeOrderByCreatedTimeDesc(user, pointType, pageable).map(PointHistoryResponseDto::from);
        }
        return pointRepository.findByUserOrderByCreatedTimeDesc(user, pageable).map(PointHistoryResponseDto::from);
    }

    private User getUser(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("해당 id의 유저가 존재하지 않습니다."));
    }

}
