package com.sy.cafe.menu.controller;

import com.sy.cafe.menu.controller.dto.MenuAddRequestDto;
import com.sy.cafe.menu.controller.dto.MenuDto;
import com.sy.cafe.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    // 전체 메뉴 조회
    @GetMapping
    public ResponseEntity<List<MenuDto>> listAllMenu(){
        return ResponseEntity.ok(menuService.listAllMenu());
    }

    // 메뉴 추가
    @PostMapping
    public ResponseEntity<MenuDto> addMenu(@RequestBody @Valid MenuAddRequestDto dto){
        return ResponseEntity.ok(menuService.addMenu(dto.getName(),dto.getPrice()));
    }

    // 메뉴 수정
    @PostMapping("/{menuId}")
    public ResponseEntity<MenuDto> updateMenu(@PathVariable Long menuId, @RequestBody @Valid MenuAddRequestDto dto){
        return ResponseEntity.ok(menuService.updateMenu(menuId, dto.getName(),dto.getPrice()));
    }


}
