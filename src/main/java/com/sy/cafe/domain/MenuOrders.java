package com.sy.cafe.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuOrders extends CreatedTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private Long price; // 메뉴 주문 가격

    @Column(nullable = false)
    private Long number; // 주문 수량


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @NotNull
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id", nullable = false)
    @NotNull
    private Orders orders;

    @Builder
    public MenuOrders(Long price, Long number, Menu menu, Orders orders) {
        this.price = price;
        this.number = number;
        this.menu = menu;
        this.orders = orders;
    }

    public static MenuOrders createMenuOrders(Menu menu, Long number, Orders order) {
        return MenuOrders.builder()
                .price(menu.getPrice())
                .number(number)
                .menu(menu)
                .orders(order)
                .build();
    }
}
