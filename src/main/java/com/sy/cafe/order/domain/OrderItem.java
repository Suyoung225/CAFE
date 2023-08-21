package com.sy.cafe.order.domain;

import com.sy.cafe.order.controller.dto.OrderItemDto;
import com.sy.cafe.global.CreatedTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class OrderItem extends CreatedTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private Long price; // 메뉴 주문 가격

    @Column(nullable = false)
    private Integer number; // 주문 수량

    @Column(nullable = false)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderItem(Long price, Integer number, Long menuId, Order order) {
        this.price = price;
        this.number = number;
        this.menuId = menuId;
        this.order = order;
    }

    public static OrderItem of(OrderItemDto dto, Order order){
        return new OrderItem(dto.getPrice(), dto.getNumber(), dto.getMenuId(), order);
    }

}
