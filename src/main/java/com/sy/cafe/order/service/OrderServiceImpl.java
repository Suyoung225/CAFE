package com.sy.cafe.order.service;

import com.sy.cafe.aop.DistributeLock;
import com.sy.cafe.menu.service.MenuService;
import com.sy.cafe.order.controller.dto.OrderItemDto;
import com.sy.cafe.order.controller.dto.OrderMenuIdNumberDto;
import com.sy.cafe.order.controller.dto.OrderResponseDto;
import com.sy.cafe.order.domain.Order;
import com.sy.cafe.order.domain.OrderDataToTransfer;
import com.sy.cafe.order.repository.OrderRepository;
import com.sy.cafe.user.service.UserServiceImpl;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final UserServiceImpl userService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderServiceImpl(OrderRepository orderRepository, MenuService menuService, UserServiceImpl userService, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @DistributeLock(key = "userId")
    public OrderResponseDto orderMenu(Long userId, List<OrderMenuIdNumberDto> orderList) {

        // 주문 메뉴 리스트
        List<OrderItemDto> orderItemList = menuService.getOrderedMenu(orderList);

        // 주문 총액
        long totalAmount = orderItemList.stream().mapToLong(i -> i.getPrice() * i.getNumber()).sum();

        // 유저 주문 가능 여부 확인, 결제
        long currentBalance = userService.order(userId, totalAmount);

        // 주문 생성
        Order order = Order.createOrder(totalAmount, userId, orderItemList);
        orderRepository.save(order);

        // 데이터 수집 플랫폼에 전송
        eventPublisher.publishEvent(new OrderEvent(OrderDataToTransfer.from(order)));

        return new OrderResponseDto(userId, currentBalance, orderList, totalAmount);
    }


    public static class OrderEvent{
        @Getter
        private final OrderDataToTransfer orderData;

        public OrderEvent(OrderDataToTransfer orderData) {
            this.orderData = orderData;
        }

    }
}
