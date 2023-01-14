package com.sy.cafe.dto.response;

import com.sy.cafe.domain.Orders;
import com.sy.cafe.domain.User;
import com.sy.cafe.dto.OrderDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    Long userId;
    Long point;
    List<OrderDto> orderList;
    Long totalAmount;

    @Builder
    public OrderResponseDto(User user, Long currentPoint, List<OrderDto> orderList, Orders order) {
        this.userId = user.getId();
        this.point = currentPoint;
        this.orderList = orderList;
        this.totalAmount = order.getAmount();
    }
}
