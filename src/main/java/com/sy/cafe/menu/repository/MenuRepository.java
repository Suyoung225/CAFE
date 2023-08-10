package com.sy.cafe.menu.repository;


import com.sy.cafe.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom{

    boolean existsByName(String name);

}
