package com.sy.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.cafe.exception.BalanceInsufficientException;
import com.sy.cafe.order.controller.OrderController;
import com.sy.cafe.order.controller.dto.OrderMenuIdNumberDto;
import com.sy.cafe.order.controller.dto.OrderRequestDto;
import com.sy.cafe.order.controller.dto.OrderResponseDto;
import com.sy.cafe.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@MockBean(JpaMetamodelMappingContext.class)
class OrderControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    OrderService orderService;

    @Test
    @DisplayName("주문")
    void order() throws Exception {
        List<OrderMenuIdNumberDto> orderList = Arrays.asList(new OrderMenuIdNumberDto(1L, 2), new OrderMenuIdNumberDto(2L,1));
        OrderRequestDto dto = new OrderRequestDto(1L, orderList);
        OrderResponseDto responseDto = new OrderResponseDto(1L, 1000L, orderList, 6000L);

        when(orderService.orderMenu(dto.getUserId(), dto.getOrderList())).thenReturn(responseDto);

        mvc.perform(post("/orders")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.currentPoint").value(1000))
                .andExpect(jsonPath("$.data.totalAmount").value(6000))
                .andExpect(jsonPath("$.data.orderList[0].menuId").value(1));
    }
    @Test
    @DisplayName("주문 시 포인트 부족")
    void pointInsufficient() throws Exception {
        List<OrderMenuIdNumberDto> orderList = Arrays.asList(new OrderMenuIdNumberDto(1L, 2), new OrderMenuIdNumberDto(2L,1));
        OrderRequestDto dto = new OrderRequestDto(1L, orderList);

        when(orderService.orderMenu(dto.getUserId(), dto.getOrderList())).thenThrow(new BalanceInsufficientException("포인트가 부족합니다."));

        mvc.perform(post("/orders")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("포인트가 부족합니다."));
    }

}