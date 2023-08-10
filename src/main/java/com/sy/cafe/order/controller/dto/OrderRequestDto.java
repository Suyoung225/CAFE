package com.sy.cafe.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    Long userId;
    List<OrderMenuIdNumberDto> orderList;
}
