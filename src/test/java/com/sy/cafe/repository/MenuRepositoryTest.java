package com.sy.cafe.repository;

import com.sy.cafe.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MenuRepositoryTest {
    @Autowired
    MenuRepository menuRepository;

    @Test
    @DisplayName("메뉴 생성")
    void createMenu(){
        // given
        Menu menu = Menu.builder()
                .id(1L)
                .name("아아")
                .price(2000L)
                .build();

        // when
        Menu menu2 = menuRepository.save(menu);

        // then
        assertThat(menu2.getPrice()).isEqualTo(menu.getPrice());
        assertThat(menu2.getId()).isEqualTo(menu.getId());
    }

    @Test
    @DisplayName("메뉴 이름 존재 여부 확인")
    void existsByName() {
        // given
        Menu menu = Menu.builder()
                .id(1L)
                .name("아아")
                .price(2000L)
                .build();
        menuRepository.save(menu);

        // when
        Boolean isExists = menuRepository.existsByName("아아");

        // then
        assertThat(isExists).isEqualTo(true);
    }
}