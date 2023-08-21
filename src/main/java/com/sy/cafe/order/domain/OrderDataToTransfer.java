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

    public static OrderDataToTransfer from(Order order){
        Long userId = order.getUserId();
        List<OrderMenuIdNumberDto> orderItems = order.getOrderItems().stream().map(OrderMenuIdNumberDto::new).collect(Collectors.toList());
        Long totalAmount = order.getAmount();
        return new OrderDataToTransfer(userId, orderItems, totalAmount);
    }

}
