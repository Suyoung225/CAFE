package com.sy.cafe.menu.service;

import com.sy.cafe.menu.controller.dto.MenuDto;
import com.sy.cafe.order.controller.dto.OrderItemDto;
import com.sy.cafe.order.controller.dto.OrderMenuIdNumberDto;

import java.util.List;

public interface MenuService {
    List<MenuDto> listAllMenu();
    MenuDto addMenu(String name, Long price);
    MenuDto updateMenu(Long menuId, String name, Long price);
    List<OrderItemDto> getOrderedMenu(List<OrderMenuIdNumberDto> orderDtoList);
}
