package com.sy.cafe.repository;

import com.sy.cafe.config.TestConfig;
import com.sy.cafe.order.domain.Order;
import com.sy.cafe.order.domain.OrderItem;
import com.sy.cafe.order.repository.OrderItemRepository;
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
class OrderItemRepositoryTest {
    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("주문 데이터 저장")
    void save(){
        // given
        Order order = Order.builder().id(1L).amount(2000L).userId(1L).build();
        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .menuId(1L)
                .price(2000L)
                .number(1)
                .order(order)
                .build();
        // when
        OrderItem orderItem2 = orderItemRepository.save(orderItem);
        // then
        assertThat(orderItem2.getId()).isEqualTo(orderItem.getId());
        assertThat(orderItem2.getNumber()).isEqualTo(orderItem.getNumber());
        assertThat(orderItem2.getMenuId()).isEqualTo(orderItem.getMenuId());
    }

}