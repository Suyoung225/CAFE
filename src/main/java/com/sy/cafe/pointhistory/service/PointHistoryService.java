package com.sy.cafe.pointhistory.service;

import com.sy.cafe.user.domain.User;

public interface PointHistoryService {
    void chargePoint(Long chargeAmount, User user);
    void usePoint(Long orderAmount, User user);
}
