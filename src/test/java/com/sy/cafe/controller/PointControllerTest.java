package com.sy.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.cafe.dto.request.PointRequestDto;
import com.sy.cafe.dto.response.PointResponseDto;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PointControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    PointService pointService;

    @Test
    @DisplayName("포인트 충전")
    void chargePoint() throws Exception {
        PointResponseDto dto = new PointResponseDto(1L, 10000L);
        PointRequestDto requestDto = new PointRequestDto(1L,2000L);
        when(pointService.chargePoint(1L,2000L)).thenReturn(dto);

        mvc.perform(post("/point/charge")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.point").value(10000L));
    }

    @Test
    @DisplayName("없는 회원 id로 포인트 충전")
    void chargeNotFoundUser() throws Exception {
        PointRequestDto requestDto = new PointRequestDto(1L,2000L);
        when(pointService.chargePoint(1L,2000L)).thenThrow(new RequestException(ErrorCode.USER_NOT_FOUND));

        mvc.perform(post("/point/charge")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("해당 유저를 찾을 수 없습니다."));
    }

}