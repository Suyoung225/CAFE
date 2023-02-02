package com.sy.cafe.service;

import com.sy.cafe.dto.OrderDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final DataTransferService dataTransferService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = OrderService.OrderEvent.class)
    public void handle(OrderService.OrderEvent event) {
        OrderDataDto orderData = event.getOrderData();
        dataTransferService.sendOrderData(orderData);
    }
}
