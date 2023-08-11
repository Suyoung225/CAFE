package com.sy.cafe.order.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemDto extends OrderMenuIdNumberDto{
    Long price;

    @Builder
    public OrderItemDto(Long menuId, int number, Long price) {
        super(menuId, number);
        this.price = price;
    }
}
