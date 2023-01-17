package com.sy.cafe.repository;

import com.sy.cafe.config.TestConfig;
import com.sy.cafe.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("주문 데이터 저장")
    void save(){
        // given
        Order order = Order.builder().id(1L).amount(2000L).userId(1L).build();
        // when
        Order order2 = orderRepository.save(order);

        // then
        assertThat(order2.getAmount()).isEqualTo(order.getAmount());
        assertThat(order2.getUserId()).isEqualTo(order.getUserId());
    }

}