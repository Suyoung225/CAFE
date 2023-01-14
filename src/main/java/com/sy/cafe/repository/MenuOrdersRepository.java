package com.sy.cafe.repository;

import com.sy.cafe.domain.MenuOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOrdersRepository extends JpaRepository<MenuOrders, Long> {
}
