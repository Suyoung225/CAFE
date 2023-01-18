package com.sy.cafe.service;

import com.sy.cafe.aop.DistributeLock;
import com.sy.cafe.domain.Order;
import com.sy.cafe.domain.OrderItem;
import com.sy.cafe.dto.OrderDto;
import com.sy.cafe.dto.OrderItemDto;
import com.sy.cafe.dto.response.OrderResponseDto;
import com.sy.cafe.repository.OrderItemRepository;
import com.sy.cafe.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuService menuService;
    private final UserService userService;


    @DistributeLock(key = "userId")
    public OrderResponseDto orderMenu(Long userId, List<OrderDto> orderList) {

        // 주문 메뉴 리스트
        List<OrderItemDto> orderItemList = menuService.orderedMenu(orderList);

        long totalAmount = 0;
        for (OrderItemDto dto : orderItemList) totalAmount += dto.getPrice() * dto.getNumber();

        // 유저 주문 가능 여부 확인, 결제
        long currentBalance = userService.order(userId, totalAmount);

        // 주문 생성
        Order order = Order.builder().amount(totalAmount).userId(userId).build();
        orderRepository.save(order);

        // 메뉴-주문 생성
        for (OrderItemDto orderItemDto : orderItemList) {
            OrderItem orderItem = new OrderItem(orderItemDto, order);
            orderItemRepository.save(orderItem);
        }

        return OrderResponseDto.builder()
                .userId(userId)
                .totalAmount(totalAmount)
                .currentPoint(currentBalance)
                .orderList(orderList)
                .build();
    }


}
