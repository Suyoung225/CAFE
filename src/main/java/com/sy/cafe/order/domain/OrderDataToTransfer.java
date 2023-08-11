package com.sy.cafe.order.domain;

import com.sy.cafe.order.controller.dto.OrderMenuIdNumberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDataToTransfer {
    Long userId;
    List<OrderMenuIdNumberDto> orderItems;
    Long totalAmount;

    public OrderDataToTransfer(Order order){
        userId = order.getUserId();
        orderItems = order.getOrderItems().stream().map(OrderMenuIdNumberDto::new).collect(Collectors.toList());
        totalAmount = order.getAmount();
    }

}
