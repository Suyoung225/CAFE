package com.sy.cafe.order.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    Long userId;
    Long currentPoint;
    List<OrderMenuIdNumberDto> orderList;
    Long totalAmount;

    public OrderResponseDto(Long userId, Long currentPoint, List<OrderMenuIdNumberDto> orderList, Long totalAmount) {
        this.userId = userId;
        this.currentPoint = currentPoint;
        this.orderList = orderList;
        this.totalAmount = totalAmount;
    }
}
