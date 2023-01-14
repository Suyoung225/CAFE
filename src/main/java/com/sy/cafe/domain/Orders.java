package com.sy.cafe.domain;

import com.sy.cafe.dto.OrderDto;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import lombok.*;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Orders extends CreatedTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private Long amount; // 총 주문(결제) 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    public Orders(Long amount, User user) {
        this.amount = amount;
        this.user = user;
    }

    public static Orders createOrder(User user, HashMap<Menu, Long> orderMenuMap) {
        long totalAmount = 0;
        for (Map.Entry<Menu, Long> entry : orderMenuMap.entrySet()) {
            totalAmount += entry.getKey().getPrice() * entry.getValue();
        }
        if(totalAmount > user.getPoint()){
            throw new RequestException(ErrorCode.BALANCE_INSUFFICIENT);
        }
        return new Orders(totalAmount, user);
    }
}
