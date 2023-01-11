package com.sy.cafe.controller;

import com.sy.cafe.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(MenuController.class)
class MenuControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    MenuService menuService;

    @Test
    void allMenu() {
    }

    @Test
    void addMenu() {
    }

    @Test
    void updateMenu() {
    }
}