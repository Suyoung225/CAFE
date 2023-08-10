package com.sy.cafe.order.service;

import com.sy.cafe.order.controller.dto.OrderResponseDto;
import com.sy.cafe.order.controller.dto.OrderMenuIdNumberDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto orderMenu(Long userId, List<OrderMenuIdNumberDto> orderList);
}
