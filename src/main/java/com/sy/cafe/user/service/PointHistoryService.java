package com.sy.cafe.user.service;

import com.sy.cafe.user.controller.dto.PointHistoryResponseDto;
import com.sy.cafe.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {
    void chargePoint(Long chargeAmount, User user);
    void usePoint(Long orderAmount, User user);
    Page<PointHistoryResponseDto> showPointHistoryByType(Long userId, String type, Pageable pageable);
}
