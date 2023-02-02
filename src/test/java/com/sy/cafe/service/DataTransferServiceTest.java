package com.sy.cafe.service;

import com.sy.cafe.dto.OrderDataDto;
import com.sy.cafe.dto.OrderDto;
import com.sy.cafe.repository.EmitterRepository;
import com.sy.cafe.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataTransferServiceTest {
    @InjectMocks
    DataTransferService dataTransferService;

    @Mock
    EmitterRepository emitterRepository;

    @Mock
    OrderRepository orderRepository;

    private String DATA_PLATFORM_ID = "DataPlatform_";

    @Test
    @DisplayName("SSE 연결")
    void subscribe() {
        // given
        SseEmitter emitter = new SseEmitter(1000L);
        when(emitterRepository.save(any(), any())).thenReturn(emitter);

        // when, then
        assertDoesNotThrow(() -> dataTransferService.subscribe(""));
    }

    @Test
    @DisplayName("SSE 연결 with 헤더")
    void subscribeWithHeader() {
        // given
        SseEmitter emitter = new SseEmitter(1000L);
        when(emitterRepository.save(any(), any())).thenReturn(emitter);

        // when, then
        assertDoesNotThrow(() -> dataTransferService.subscribe(DATA_PLATFORM_ID + "1675313587027"));
        verify(orderRepository, times(1)).findBycreatedTimeAfter(any());
    }

    @Test
    @DisplayName("주문 데이터 전송")
    void sendOrderData() {
        // given
        List<OrderDto> orderList = Arrays.asList(new OrderDto(1L, 2), new OrderDto(2L,1));
        OrderDataDto orderData = new OrderDataDto(1L, orderList, 4000L);

        SseEmitter sseEmitter = new SseEmitter(60L * 1000 * 60);
        String emitterId = DATA_PLATFORM_ID + System.currentTimeMillis();
        Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
        sseEmitters.put(emitterId, sseEmitter);

        when(emitterRepository.findAllEmitters(DATA_PLATFORM_ID)).thenReturn(sseEmitters);

        // when, then
        assertDoesNotThrow(() -> dataTransferService.sendOrderData(orderData));
    }

}