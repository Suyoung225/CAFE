package com.sy.cafe.repository;


import com.sy.cafe.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    boolean existsByName(String name);
}
