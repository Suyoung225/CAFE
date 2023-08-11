package com.sy.cafe.order.controller;

import com.sy.cafe.order.controller.dto.OrderRequestDto;
import com.sy.cafe.order.controller.dto.OrderResponseDto;
import com.sy.cafe.global.ResponseDto;
import com.sy.cafe.order.service.OrderService;
import com.sy.cafe.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponseDto> orderMenu(@RequestBody OrderRequestDto dto){
        return ResponseEntity.ok(orderService.orderMenu(dto.getUserId(), dto.getOrderList()));
    }
}
