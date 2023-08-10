package com.sy.cafe.order.repository;

import com.sy.cafe.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCreatedTimeAfter(LocalDateTime datetime);
}
