package com.sy.cafe.service;

import com.sy.cafe.aop.UpdateLock;
import com.sy.cafe.domain.Menu;
import com.sy.cafe.dto.OrderDto;
import com.sy.cafe.dto.OrderItemDto;
import com.sy.cafe.dto.response.MenuResponseDto;
import com.sy.cafe.dto.response.PopularMenuDto;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final PopularMenuService popularMenuService;

    // 전체 메뉴 조회
    @Transactional(readOnly = true)
    public List<MenuResponseDto> allMenu(){
        return menuRepository.findAll().stream().map(MenuResponseDto::new).collect(Collectors.toList());
    }

    // 메뉴 추가
    @Transactional
    public MenuResponseDto addMenu(String name, Long price) {
        if(menuRepository.existsByName(name))
            throw new RequestException(ErrorCode.ALREADY_EXISTS);
        Menu menu = new Menu(name, price);
        menuRepository.save(menu);
        return new MenuResponseDto(menu);
    }

    // 메뉴 수정
    @Transactional
    public MenuResponseDto updateMenu(Long menuId, String name, Long price) {
        // 입력 받은 id 존재여부
        Menu menu = getMenu(menuId);

        // 수정한 메뉴이름이 이미 존재하는 경우
        if(menuRepository.existsByName(name) && !menu.getName().equals(name))
            throw new RequestException(ErrorCode.ALREADY_EXISTS);

        menu.update(name, price);
        return new MenuResponseDto(menu);
    }

    // 주문한 메뉴 리턴
    @Transactional(readOnly = true)
    public List<OrderItemDto> orderedMenu(List<OrderDto> orderDtos){
        List<OrderItemDto> orderItemDtos = new LinkedList<>();
        for (OrderDto orderDto : orderDtos) {
            Menu menu = getMenu(orderDto.getMenuId());
            orderItemDtos.add(new OrderItemDto(menu.getId(),orderDto.getNumber(),menu.getPrice()));
        }
        return orderItemDtos;
    }

    private Menu getMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(
                () -> new RequestException(ErrorCode.MENU_NOT_FOUND));
    }

    // 인기 메뉴 조회
    @Transactional(readOnly = true)
    @Cacheable(value = "menu", cacheManager = "cacheManager")
    public List<PopularMenuDto> popularMenu() {
        return menuRepository.popularMenus();
    }

    // 인기 메뉴 캐시 삭제 및 추가
    @UpdateLock
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateCache() {
        popularMenuService.setPopularMenuCache();
    }

}
