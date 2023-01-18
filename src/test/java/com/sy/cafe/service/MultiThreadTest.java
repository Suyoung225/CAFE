package com.sy.cafe.service;

import com.sy.cafe.RedisTestContainer;
import com.sy.cafe.domain.Menu;
import com.sy.cafe.domain.User;
import com.sy.cafe.dto.OrderDto;
import com.sy.cafe.repository.MenuRepository;
import com.sy.cafe.repository.PointRepository;
import com.sy.cafe.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MultiThreadTest extends RedisTestContainer {
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    MenuRepository menuRepository;

    @BeforeEach
    public void beforeEach() {
        menuRepository.deleteAll();
        pointRepository.deleteAll();
        userRepository.deleteAll();
        User user = User.builder().nickname("sy").point(10000L).build();
        userRepository.save(user);
        Menu menu = Menu.builder().price(500L).name("아아").build();
        menuRepository.save(menu);
    }


    @Test
    @DisplayName("동시 주문")
    void Order() throws InterruptedException{
        Long userId = userRepository.findAll().get(0).getId();
        Long menuId = menuRepository.findAll().get(0).getId();
        List<OrderDto> orderList = List.of(new OrderDto(menuId,1));
        final int THREAD_COUNT = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // when
        IntStream.range(0, THREAD_COUNT).forEach(e -> executorService.submit(() -> {
                    try {
                        orderService.orderMenu(userId,orderList);
                    } finally {
                        latch.countDown();
                    }
                }
        ));
        latch.await();

        User user = userRepository.findAll().get(0);
        long point = user.getPoint();

        // then
        assertThat(point).isEqualTo(0L);
    }

    @Test
    @DisplayName("동시 충전")
    void multiThreadCharge() throws InterruptedException{
        Long id = userRepository.findAll().get(0).getId();
        final int THREAD_COUNT = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // when
        IntStream.range(0, THREAD_COUNT).forEach(e -> executorService.submit(() -> {
                    try {
                        userService.chargePoint(id,100L);
                    } finally {
                        latch.countDown();
                    }
                }
        ));
        latch.await();

        User user = userRepository.findAll().get(0);
        long point = user.getPoint();

        // then
        assertThat(point).isEqualTo(20000L);
    }
}
