package com.sy.cafe.service;

import com.sy.cafe.dto.OrderDto;
import com.sy.cafe.dto.OrderItemDto;
import com.sy.cafe.dto.response.OrderResponseDto;
import com.sy.cafe.repository.OrderItemRepository;
import com.sy.cafe.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderService orderService;

    @Mock
    UserService userService;
    @Mock
    MenuService menuService;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("주문 성공")
    void orderMenu() {
        // given
        List<OrderDto> orderList = Arrays.asList(new OrderDto(1L, 2), new OrderDto(2L,1));
        List<OrderItemDto> orderItemList = Arrays.asList(
                new OrderItemDto(1L, 2,2000L),
                new OrderItemDto(2L,1,2000L));
        when(menuService.orderedMenu(orderList)).thenReturn(orderItemList);
        when(userService.order(1L, 6000L)).thenReturn(1000L);

        // when
        OrderResponseDto orderResponseDto = orderService.orderMenu(1L, orderList);

        // then
        assertThat(orderResponseDto.getCurrentPoint()).isEqualTo(1000L);
        assertThat(orderResponseDto.getOrderList()).isEqualTo(orderList);
        assertThat(orderResponseDto.getTotalAmount()).isEqualTo(6000L);
    }
}