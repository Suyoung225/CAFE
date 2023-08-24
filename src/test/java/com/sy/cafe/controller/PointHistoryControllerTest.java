package com.sy.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.cafe.user.controller.PointHistoryController;
import com.sy.cafe.user.controller.dto.PointHistoryResponseDto;
import com.sy.cafe.user.service.PointHistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointHistoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PointHistoryControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    PointHistoryService pointHistoryService;


    @Test
    @DisplayName("포인트 이력 타입별 조회")
    void showPointHistory() throws Exception {
        PointHistoryResponseDto dto1 = new PointHistoryResponseDto(10000L, "CHARGE", LocalDateTime.now());
        PointHistoryResponseDto dto2 = new PointHistoryResponseDto(5000L, "PAYMENT", LocalDateTime.now());
        List<PointHistoryResponseDto> dtos = new ArrayList<>();
        dtos.add(dto1);
        dtos.add(dto2);
        Page<PointHistoryResponseDto> page = new PageImpl<>(dtos);
        Pageable pageable = Pageable.ofSize(5);
        when(pointHistoryService.showPointHistoryByType(1L, "CHARGE", pageable))
                .thenReturn(page);
        mvc.perform(get("/point?userId=1&type=CHARGE")
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("전체 포인트 조회")
    void showPointHistoryByType() throws Exception{
        PointHistoryResponseDto dto1 = new PointHistoryResponseDto(10000L, "CHARGE", LocalDateTime.now());
        PointHistoryResponseDto dto2 = new PointHistoryResponseDto(5000L, "PAYMENT", LocalDateTime.now());
        List<PointHistoryResponseDto> dtos = new ArrayList<>();
        dtos.add(dto1);
        dtos.add(dto2);
        Page<PointHistoryResponseDto> page = new PageImpl<>(dtos);
        Pageable pageable = Pageable.ofSize(5);
        when(pointHistoryService.showPointHistoryByType(1L, null, pageable))
                .thenReturn(page);
        mvc.perform(get("/point?userId=1")
                        .contentType("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }
}