package com.sy.cafe.repository;

import com.sy.cafe.config.TestConfig;
import com.sy.cafe.menu.domain.Menu;
import com.sy.cafe.order.domain.Order;
import com.sy.cafe.order.domain.OrderItem;
import com.sy.cafe.menu.controller.dto.PopularMenuDto;
import com.sy.cafe.menu.repository.MenuRepository;
import com.sy.cafe.order.repository.OrderItemRepository;
import com.sy.cafe.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import({TestConfig.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MenuQuerydslTest {
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;
    @MockBean
    DateTimeProvider dateTimeProvider;
    @SpyBean
    private AuditingHandler handler;

    @BeforeEach
    public void beforeEach() {
        menuRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        handler.setDateTimeProvider(dateTimeProvider);
    }

    @Test
    @DisplayName("인기 메뉴")
    void popularMenu(){
        // given
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(time));

        Menu menu1 = new Menu("메뉴1",2000L);
        Menu menu2 = new Menu("메뉴2",2000L);
        Menu menu3 = new Menu("메뉴3",2000L);

        Long menuId1 = menuRepository.save(menu1).getId();
        Long menuId2 = menuRepository.save(menu2).getId();
        Long menuId3 = menuRepository.save(menu3).getId();

        Order order1 = new Order(5000L, 1L);
        Order order2 = new Order(4000L, 1L);
        Order order3 = new Order(8000L, 1L);
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        OrderItem orderItem1 = OrderItem.builder().order(order1).menuId(menuId1).number(1).price(2000L).build();
        OrderItem orderItem2 = OrderItem.builder().order(order1).menuId(menuId3).number(1).price(3000L).build();
        OrderItem orderItem3 = OrderItem.builder().order(order2).menuId(menuId1).number(2).price(2000L).build();
        OrderItem orderItem4 = OrderItem.builder().order(order1).menuId(menuId2).number(4).price(2000L).build();

        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);
        orderItemRepository.save(orderItem3);

        time = LocalDateTime.now().minusDays(8);
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(time));
        orderItemRepository.save(orderItem4); // 메뉴2는 주간 랭킹에서 제외

        // when
        List<PopularMenuDto> list = menuRepository.popularMenus();
        assertThat(list.get(0).getName()).isEqualTo("메뉴1");
        assertThat(list.get(0).getOrderNum()).isEqualTo(3L);
        assertThat(list.get(1).getName()).isEqualTo("메뉴3");
        assertThat(list.get(1).getOrderNum()).isEqualTo(1L);
        assertThat(list.size()).isEqualTo(2);
    }

}