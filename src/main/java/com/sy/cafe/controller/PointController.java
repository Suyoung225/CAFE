package com.sy.cafe.controller;

import com.sy.cafe.dto.request.PointRequestDto;
import com.sy.cafe.dto.response.PointResponseDto;
import com.sy.cafe.global.ResponseDto;
import com.sy.cafe.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/point")
public class PointController {
    private final PointService pointService;

    @PostMapping("/charge")
    public ResponseDto<PointResponseDto> chargePoint(@RequestBody PointRequestDto dto){
        return ResponseDto.success(pointService.chargePoint(dto.getUserId(), dto.getPoint()));
    }
}
