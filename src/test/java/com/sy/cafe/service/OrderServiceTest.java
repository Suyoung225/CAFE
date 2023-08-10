package com.sy.cafe.service;

import com.sy.cafe.menu.service.MenuService;
import com.sy.cafe.order.controller.dto.OrderMenuIdNumberDto;
import com.sy.cafe.order.controller.dto.OrderItemDto;
import com.sy.cafe.order.controller.dto.OrderResponseDto;
import com.sy.cafe.menu.service.MenuServiceImpl;
import com.sy.cafe.order.service.OrderService;
import com.sy.cafe.order.service.OrderServiceImpl;
import com.sy.cafe.order.repository.OrderItemRepository;
import com.sy.cafe.order.repository.OrderRepository;
import com.sy.cafe.user.service.UserService;
import com.sy.cafe.user.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderServiceImpl orderService;
    @Mock
    UserServiceImpl userService;
    @Mock
    MenuServiceImpl menuService;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderItemRepository orderItemRepository;
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Test
    @DisplayName("주문 성공")
    void orderMenu() {
        // given
        List<OrderMenuIdNumberDto> orderList = Arrays.asList(new OrderMenuIdNumberDto(1L, 2), new OrderMenuIdNumberDto(2L,1));
        List<OrderItemDto> orderItemList = Arrays.asList(
                new OrderItemDto(1L, 2,2000L),
                new OrderItemDto(2L,1,2000L));
        when(menuService.getOrderedMenu(orderList)).thenReturn(orderItemList);
        when(userService.order(1L, 6000L)).thenReturn(1000L);

        // when
        OrderResponseDto orderResponseDto = orderService.orderMenu(1L, orderList);

        // then
        assertThat(orderResponseDto.getCurrentPoint()).isEqualTo(1000L);
        assertThat(orderResponseDto.getOrderList()).isEqualTo(orderList);
        assertThat(orderResponseDto.getTotalAmount()).isEqualTo(6000L);
    }
}