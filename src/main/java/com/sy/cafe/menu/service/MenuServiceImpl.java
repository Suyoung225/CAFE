package com.sy.cafe.menu.service;

import com.sy.cafe.exception.DuplicatedMenuException;
import com.sy.cafe.exception.MenuNotFoundException;
import com.sy.cafe.menu.controller.dto.MenuDto;
import com.sy.cafe.menu.domain.Menu;
import com.sy.cafe.menu.repository.MenuRepository;
import com.sy.cafe.order.controller.dto.OrderItemDto;
import com.sy.cafe.order.controller.dto.OrderMenuIdNumberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl implements MenuService{
    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // 전체 메뉴 조회
    @Override
    @Transactional(readOnly = true)
    public List<MenuDto> listAllMenu(){
        return menuRepository.findAll().stream().map(MenuDto::from).collect(Collectors.toList());
    }

    // 메뉴 추가
    @Override
    @Transactional
    public MenuDto addMenu(String name, Long price) {
        // 추가하려는 메뉴의 이름이 존재하는 경우
        if(menuRepository.existsByName(name))
            throw new DuplicatedMenuException("메뉴가 존재합니다.");
        Menu menu = new Menu(name, price);
        menuRepository.save(menu);
        return MenuDto.from(menu);
    }

    // 메뉴 수정
    @Override
    @Transactional
    public MenuDto updateMenu(Long menuId, String name, Long price) {
        // 입력 받은 id 존재 여부
        Menu menu = getMenu(menuId);

        // 수정한 메뉴 이름이 존재하는 경우
        if(menuRepository.existsByName(name) && !menu.getName().equals(name))
            throw new DuplicatedMenuException("메뉴가 존재합니다.");

        menu.update(name, price);
        return MenuDto.from(menu);
    }

    // 주문한 메뉴 가격까지 포함해서 리턴
    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> getOrderedMenu(List<OrderMenuIdNumberDto> orderDtoList){
        return orderDtoList.stream()
                .map(dto -> {
                    Menu menu = getMenu(dto.getMenuId());
                    return new OrderItemDto(menu.getId(), dto.getNumber(), menu.getPrice());
                }).collect(Collectors.toList());
    }

    private Menu getMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(
                () -> new MenuNotFoundException("해당 id의 메뉴가 존재하지 않습니다."));
    }

}
