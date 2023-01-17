package com.sy.cafe.service;

import com.sy.cafe.domain.User;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    PointService pointService;

    @Test
    @DisplayName("회원 추가")
    void register() {
        // given
        User user = new User("수영");
        when(userRepository.save(any())).thenReturn(user);

        // when
        String nickname = userService.register("수영").getNickname();
        Long point = userService.register("수영").getPoint();

        // then
        assertThat(nickname).isEqualTo("수영");
        assertThat(point).isEqualTo(0L);
    }
    @Test
    @DisplayName("포인트 충전")
    void chargePoint(){
        User user = User.builder().id(1L).nickname("sy").point(1000L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        // when
        Long pointAfterCharge = userService.chargePoint(1L,5000L).getPoint();

        // then
        verify(pointService,times(1)).chargePoint(any(),any());
        assertThat(pointAfterCharge).isEqualTo(6000L);
    }

    @Test
    @DisplayName("존재하지 않는 회원 아이디")
    void userIdNotFound(){

        when(userRepository.findById(1L)).thenThrow(new RequestException(ErrorCode.USER_NOT_FOUND));

        // when
        RequestException exception = assertThrows(RequestException.class, ()-> {
            userService.chargePoint(1L, 5000L); });

        // then
        verify(pointService, never()).chargePoint(any(),any());
        assertThat(exception.getMessage()).isEqualTo("해당 유저를 찾을 수 없습니다.");
    }
    @Test
    @DisplayName("주문 성공")
    void order() {
        // given
        User user = User.builder().id(1L).nickname("sy").point(10000L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        // when
        long point = userService.order(1L,4000L);

        // then
        verify(pointService,times(1)).usePoint(any(),any());
        assertThat(point).isEqualTo(6000L);
    }

    @Test
    @DisplayName("주문 시 잔고 부족")
    void insufficientPoint(){
        // given
        User user = User.builder().id(1L).nickname("sy").point(2000L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        // when
        RequestException exception = assertThrows(RequestException.class, ()-> {
            userService.order(1L, 5000L); });
        // then
        verify(pointService, never()).chargePoint(any(),any());
        assertThat(exception.getMessage()).isEqualTo("포인트가 부족합니다.");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

}