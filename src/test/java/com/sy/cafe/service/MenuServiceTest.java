package com.sy.cafe.service;

import com.sy.cafe.domain.Menu;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    MenuService menuService;

    @Mock
    MenuRepository menuRepository;

    @Test
    @DisplayName("메뉴 추가")
    void addMenu(){
        // given
        String name = "콜드브루";
        Long price = 4000L;
        Menu menu = new Menu(name, price);
        when(menuRepository.save(any())).thenReturn(menu);

        // when
        String newMenuName = menuService.addMenu(name, price).getName();

        // then
        assertThat(newMenuName).isEqualTo(name);
        // Id 생성 전략을 Identity를 사용하므로, 실제 DBd에 저장되야만 Id가 생성된다. 따라서 테스트에서 Id를 검증할 수 없다.
    }

    @Test
    @DisplayName("메뉴 중복하여 추가")
    void addDuplicatedMenu(){
        // given
        String name = "콜드브루";
        Long price = 4000L;

        when(menuRepository.existsByName(name)).thenReturn(true);

        // when
        RequestException exception = assertThrows(RequestException.class, ()-> {
            menuService.addMenu(name, price); });

        // then
        assertThat("이미 존재하는 데이터입니다.").isEqualTo(exception.getMessage());

    }

    @Test
    @DisplayName("전체 메뉴 조회")
    void allMenu() {
        // given
        Menu menu1 = new Menu("아아",2000L);
        Menu menu2 = new Menu("따아",2000L);
        List<Menu> menuList = Arrays.asList(menu1, menu2);
        when(menuRepository.findAll()).thenReturn(menuList);

        // when
        String menu1Name = menuService.allMenu().get(0).getName();
        String menu2Name = menuService.allMenu().get(1).getName();

        // then
        assertThat(menu1Name).isEqualTo("아아");
        assertThat(menu2Name).isEqualTo("따아");
    }

    @Test
    @DisplayName("메뉴 수정 - 이름 변경")
    void updateMenuName() {
        // given
        String name = "콜드브루";
        String name2 = "콜드브루22";
        Long price = 4000L;
        Menu menu = new Menu(name, price);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.existsByName(name2)).thenReturn(false);

        // when
        String newMenuName = menuService.updateMenu(1L, name2, price).getName();

        // then
        assertThat(newMenuName).isEqualTo(name2);
    }

    @Test
    @DisplayName("메뉴 수정 - 가격 변경")
    void updateMenuPrice() {
        // given
        String name = "콜드브루";
        Long price = 4000L;
        Menu menu = new Menu(name, price);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.existsByName(name)).thenReturn(true);

        Long price2 = 3000L;

        // when
        Long newMenuPrice = menuService.updateMenu(1L, name, price2).getPrice();

        // then
        assertThat(newMenuPrice).isEqualTo(price2);
    }

    @Test
    @DisplayName("중복된 이름으로 메뉴이름 수정")
    void updateDuplicatedMenu() {
        // given
        String name = "콜드브루";
        String name2 = "아아";
        Long price = 4000L;
        Menu menu = new Menu(name, price);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.existsByName(name2)).thenReturn(true);


        // when
        RequestException exception = assertThrows(RequestException.class, ()-> {
        menuService.updateMenu(1L, name2, price); });

        // then
        assertThat("이미 존재하는 데이터입니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 메뉴id")
    void updateMenuWithWrongID() {
        // given
        when(menuRepository.findById(1L)).thenThrow(new RequestException(ErrorCode.MENU_NOT_FOUND));

        // when
        RequestException exception = assertThrows(RequestException.class, ()-> {
            menuService.updateMenu(1L, "아아", 2000L); });

        // then
        assertThat("해당 메뉴를 찾을 수 없습니다.").isEqualTo(exception.getMessage());
    }

}