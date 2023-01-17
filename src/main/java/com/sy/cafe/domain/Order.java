package com.sy.cafe.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "orders")
public class Order extends CreatedTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private Long amount; // 총 주문(결제) 가격

    @Column(nullable = false)
    private Long userId;

    @Builder
    public Order(Long amount, Long userId) {
        this.amount = amount;
        this.userId = userId;
    }

}
