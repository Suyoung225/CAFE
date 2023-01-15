package com.sy.cafe.service;

import com.sy.cafe.domain.*;
import com.sy.cafe.dto.OrderDto;
import com.sy.cafe.dto.response.OrderResponseDto;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final MenuRepository menuRepository;
    private final MenuOrdersRepository menuOrdersRepository;

    @Transactional
    public OrderResponseDto orderMenu(Long userId, List<OrderDto> orderList) {
        // 입력 받은 id 존재여부
        User user = findUser(userId);

        HashMap<Menu, Long> orderMenuMap = new HashMap<>();
        for (OrderDto orderDto : orderList) {
            Menu menu = findMenu(orderDto.getMenuId());
            orderMenuMap.put(menu, orderDto.getNumber());
        }
        // 주문 생성
        Orders order = Orders.createOrder(user, orderMenuMap);
        orderRepository.save(order);

        // 메뉴-주문 생성
        for (Map.Entry<Menu, Long> orderMap : orderMenuMap.entrySet()) {
            MenuOrders menuOrders =
                    MenuOrders.createMenuOrders(orderMap.getKey(), orderMap.getValue(), order);
            menuOrdersRepository.save(menuOrders);
        }
        // 포인트 차감
        long currentPoint = user.usePoint(order.getAmount());
        // 포인트 사용 이력 추가
        Point point = Point.builder()
                .type(PointType.PAYMENT)
                .user(user)
                .point(order.getAmount())
                .build();
        pointRepository.save(point);

        return OrderResponseDto.builder()
                .user(user)
                .order(order)
                .currentPoint(currentPoint)
                .orderList(orderList)
                .build();
    }

    private User findUser(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_FOUND));
    }
    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(
                () -> new RequestException(ErrorCode.MENU_NOT_FOUND));
    }


}
