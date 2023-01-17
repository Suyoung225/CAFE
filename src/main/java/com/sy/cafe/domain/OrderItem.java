package com.sy.cafe.domain;

import com.sy.cafe.dto.OrderItemDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class OrderItem extends CreatedTimeEntity{

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
    @NotNull
    private Order order;

    public OrderItem(OrderItemDto dto, Order order) {
        this.price = dto.getPrice();
        this.number = dto.getNumber();
        this.menuId = dto.getMenuId();
        this.order = order;
    }


}
