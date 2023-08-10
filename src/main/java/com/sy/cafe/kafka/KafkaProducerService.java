package com.sy.cafe.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.cafe.order.domain.OrderDataToTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    public void sendOrderData(OrderDataToTransfer orderData){
        String topic = "orderData";
        try{
            String orderDataJson = mapper.writeValueAsString(orderData);
            kafkaTemplate.send(topic, orderDataJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
