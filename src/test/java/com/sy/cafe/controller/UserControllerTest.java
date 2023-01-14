package com.sy.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.cafe.domain.User;
import com.sy.cafe.dto.request.UserRegisterDto;
import com.sy.cafe.dto.response.UserResponseDto;
import com.sy.cafe.service.UserService;
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
        UserResponseDto dto = new UserResponseDto(User.builder().id(1L).nickname("회원1").point(0L).build());
        when(userService.register("회원1")).thenReturn(dto);

        mvc.perform(post("/user/register")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(new UserRegisterDto("회원1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.nickname").value("회원1"))
                .andExpect(jsonPath("$.data.point").value(0L));
    }

}