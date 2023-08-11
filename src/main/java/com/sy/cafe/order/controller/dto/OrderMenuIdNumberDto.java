package com.sy.cafe.order.controller.dto;

import com.sy.cafe.order.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuIdNumberDto {
    Long menuId;
    int number;

    public OrderMenuIdNumberDto(OrderItem orderItem){
        this.menuId = orderItem.getMenuId();
        this.number = orderItem.getNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrderMenuIdNumberDto))
            return false;
        OrderMenuIdNumberDto dto = (OrderMenuIdNumberDto)o;
        return Objects.equals(menuId, dto.menuId) &&
                Objects.equals(number, dto.number);
    }
}
