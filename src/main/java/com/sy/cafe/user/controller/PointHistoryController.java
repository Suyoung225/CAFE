package com.sy.cafe.user.controller;

import com.sy.cafe.user.controller.dto.PointHistoryResponseDto;
import com.sy.cafe.user.service.PointHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointHistoryController {
    private final PointHistoryService pointHistoryService;

    public PointHistoryController(PointHistoryService pointHistoryService) {
        this.pointHistoryService = pointHistoryService;
    }

    // 포인트 이력 type별로 조회
    @GetMapping()
    public ResponseEntity<Page<PointHistoryResponseDto>> showPointHistoryByType(@RequestParam Long userId,
                                                                                @RequestParam(required = false) String type,
                                                                                @PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(pointHistoryService.showPointHistoryByType(userId, type, pageable));
    }

}
