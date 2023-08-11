package com.sy.cafe.pointhistory.service;

import com.sy.cafe.pointhistory.domain.PointHistory;
import com.sy.cafe.pointhistory.domain.PointType;
import com.sy.cafe.user.domain.User;
import com.sy.cafe.pointhistory.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointHistoryServiceImpl implements PointHistoryService{
    private final PointRepository pointRepository;

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

}
