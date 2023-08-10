package com.sy.cafe.menu.controller;

import com.sy.cafe.menu.controller.dto.PopularMenuDto;
import com.sy.cafe.menu.service.PopularMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PopularMenuController {
    private final PopularMenuService popularMenuService;

    public PopularMenuController(PopularMenuService popularMenuService) {
        this.popularMenuService = popularMenuService;
    }

    // 인기 메뉴
    @GetMapping("/menu/popular")
    public ResponseEntity<List<PopularMenuDto>> popularMenu(){
        return ResponseEntity.ok(popularMenuService.top3PopularMenusIn7Days());
    }
}
