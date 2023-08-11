package com.sy.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.cafe.exception.UserNotFoundException;
import com.sy.cafe.user.controller.dto.UserPointDto;
import com.sy.cafe.user.controller.UserController;
import com.sy.cafe.user.controller.dto.PointChargeRequestDto;
import com.sy.cafe.user.controller.dto.UserDto;
import com.sy.cafe.user.controller.dto.UserRegisterDto;
import com.sy.cafe.user.domain.User;
import com.sy.cafe.user.service.UserService;
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


@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("회원 추가")
    void register() throws Exception {
        UserDto dto = new UserDto(User.builder().id(1L).nickname("회원1").point(0L).build());
        when(userService.registerUser("회원1")).thenReturn(dto);

        mvc.perform(post("/user/register")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(new UserRegisterDto("회원1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.nickname").value("회원1"))
                .andExpect(jsonPath("$.point").value(0L));
    }
    @Test
    @DisplayName("포인트 충전")
    void chargePoint() throws Exception {
        UserPointDto dto = new UserPointDto(1L, 10000L);
        PointChargeRequestDto requestDto = new PointChargeRequestDto(1L,2000L);
        when(userService.chargePoint(new PointChargeRequestDto(1L,2000L))).thenReturn(dto);

        mvc.perform(post("/point/charge")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.point").value(10000L));
    }

    @Test
    @DisplayName("없는 회원 id로 포인트 충전")
    void chargeNotFoundUser() throws Exception {
        PointChargeRequestDto requestDto = new PointChargeRequestDto(1L,2000L);
        when(userService.chargePoint(new PointChargeRequestDto(1L,2000L))).thenThrow(new UserNotFoundException("해당 id의 유저가 존재하지 않습니다."));

        mvc.perform(post("/point/charge")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 id의 유저가 존재하지 않습니다."));
    }

}