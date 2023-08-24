package com.sy.cafe.service;

import com.sy.cafe.menu.domain.Menu;
import com.sy.cafe.user.repository.PointRepository;
import com.sy.cafe.user.controller.dto.PointChargeRequestDto;
import com.sy.cafe.user.domain.User;
import com.sy.cafe.order.controller.dto.OrderMenuIdNumberDto;
import com.sy.cafe.menu.repository.MenuRepository;
import com.sy.cafe.order.repository.OrderItemRepository;
import com.sy.cafe.order.repository.OrderRepository;
import com.sy.cafe.order.service.OrderServiceImpl;
import com.sy.cafe.user.repository.UserRepository;
import com.sy.cafe.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
public class MultiThreadTest{
    @Autowired
    UserServiceImpl userService;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;


    @BeforeEach
    public void beforeEach() {
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
        List<OrderMenuIdNumberDto> orderList = List.of(new OrderMenuIdNumberDto(menuId,1));
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
                        userService.chargePoint(new PointChargeRequestDto(id,100L));
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

    @Test
    @DisplayName("동시 충전과 주문 성공")
    void multiThreadChargeAndOrderSuccess() {
        Long userId = userRepository.findAll().get(0).getId();
        Long menuId = menuRepository.findAll().get(0).getId();
        List<OrderMenuIdNumberDto> orderList = List.of(new OrderMenuIdNumberDto(menuId,20)); // total:10,000원

        final int THREAD_COUNT = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        IntStream.range(0, THREAD_COUNT).forEach(e -> executorService.submit(() -> {
            CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
                orderService.orderMenu(userId,orderList);
                System.out.println("Future order Thread: " + Thread.currentThread().getName());
            });
            System.out.println("order Thread: " + Thread.currentThread().getName());

            CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
                userService.chargePoint(new PointChargeRequestDto(userId,10000L));
                System.out.println("Future charge Thread: " + Thread.currentThread().getName());
            });
            System.out.println("charge Thread: " + Thread.currentThread().getName());

            try {
                future1.get();
                future2.get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
            latch.countDown();
            }

        ));

        User user = userRepository.findAll().get(0);
        long point = user.getPoint();
        System.out.println(point);
        System.out.println(user.getId());

        // then
        assertThat(point).isEqualTo(10000L);
    }

    @Test
    @DisplayName("동시 충전과 주문 실패")
    void multiThreadChargeAndOrderFail() throws InterruptedException, ExecutionException {
        Long userId = userRepository.findAll().get(0).getId();
        Long menuId = menuRepository.findAll().get(0).getId();
        List<OrderMenuIdNumberDto> orderList = List.of(new OrderMenuIdNumberDto(menuId,40)); // total:20,000원

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            orderService.orderMenu(userId,orderList);
            System.out.println("Future order Thread: " + Thread.currentThread().getName());
        });
        try{
            future1.get();
            fail();
        }catch(ExecutionException e){
            System.out.println(e.getMessage());
        }

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            userService.chargePoint(new PointChargeRequestDto(userId,10000L));
            System.out.println("Future charge Thread: " + Thread.currentThread().getName());
        });
        future2.get();

    }

}
