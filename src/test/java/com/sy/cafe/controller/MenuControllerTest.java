package com.sy.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.cafe.domain.Menu;
import com.sy.cafe.dto.request.MenuRequestDto;
import com.sy.cafe.dto.response.MenuResponseDto;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MenuControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    MenuService menuService;

    @Test
    @DisplayName("전체 메뉴 조회")
    void allMenu() throws Exception {
        Menu menu1 = Menu.builder().id(1L).price(2000L).name("메뉴1").build();
        Menu menu2 = Menu.builder().id(2L).price(2000L).name("메뉴2").build();
        List<MenuResponseDto> list = List.of(new MenuResponseDto(menu1), new MenuResponseDto(menu2));
        when(menuService.allMenu()).thenReturn(list);

        mvc.perform(get("/menu")
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("메뉴1"))
                .andExpect(jsonPath("$.data[1].name").value("메뉴2"));
    }

    @Test
    @DisplayName("메뉴 추가")
    void addMenu() throws Exception {
        MenuResponseDto dto = new MenuResponseDto(Menu.builder().id(1L).price(2000L).name("메뉴1").build());
        MenuRequestDto requestDto = new MenuRequestDto("메뉴1",2000L);
        when(menuService.addMenu("메뉴1",2000L)).thenReturn(dto);

        mvc.perform(post("/menu")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("메뉴1"))
                .andExpect(jsonPath("$.data.price").value(2000L));
    }
    @Test
    @DisplayName("중복된 이름의 메뉴 추가")
    void addMenuDuplicatedName() throws Exception {
        MenuRequestDto requestDto = new MenuRequestDto("메뉴1",2000L);
        when(menuService.addMenu("메뉴1",2000L)).thenThrow(new RequestException(ErrorCode.ALREADY_EXISTS));

        mvc.perform(post("/menu")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("이미 존재하는 데이터입니다."));
    }

    @Test
    @DisplayName("메뉴 변경")
    void updateMenu() throws Exception{
        MenuResponseDto dto = new MenuResponseDto(Menu.builder().id(1L).price(2000L).name("메뉴11").build());
        MenuRequestDto requestDto = new MenuRequestDto("메뉴11",2000L);
        when(menuService.updateMenu(1L,"메뉴11",2000L)).thenReturn(dto);

        mvc.perform(post("/menu/1")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("메뉴11"))
                .andExpect(jsonPath("$.data.price").value(2000L));
    }

}