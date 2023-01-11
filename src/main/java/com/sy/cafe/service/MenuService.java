package com.sy.cafe.service;

import com.sy.cafe.domain.Menu;
import com.sy.cafe.dto.response.MenuResponseDto;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class MenuService {
    private final MenuRepository menuRepository;

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
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new RequestException(ErrorCode.MENU_NOT_FOUND));

        // 수정한 메뉴이름이 이미 존재하는 경우
        if(menuRepository.existsByName(name) && !menu.getName().equals(name))
            throw new RequestException(ErrorCode.ALREADY_EXISTS);

        menu.update(name, price);
        return new MenuResponseDto(menu);
    }

}
