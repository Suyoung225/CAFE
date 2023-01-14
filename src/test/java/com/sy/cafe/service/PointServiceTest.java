package com.sy.cafe.service;

import com.sy.cafe.domain.Point;
import com.sy.cafe.domain.PointType;
import com.sy.cafe.domain.User;
import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import com.sy.cafe.repository.PointRepository;
import com.sy.cafe.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService pointService;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("포인트 충전")
    void charge(){
        // given
        User user = User.builder().id(1L).point(1000L).nickname("sy").build();
        Point point = Point.builder()
                .type(PointType.CHARGE)
                .user(user)
                .point(5000L)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(pointRepository.save(any())).thenReturn(point);
        // when
        Long pointAfterCharge = pointService.chargePoint(1L,5000L).getPoint();
        // then
        assertThat(pointAfterCharge).isEqualTo(6000L);
    }

    @Test
    @DisplayName("없는 회원 포인트 충전")
    void chargeNotFoundUser(){
        // given
        when(userRepository.findById(1L)).thenThrow(new RequestException(ErrorCode.USER_NOT_FOUND));
        // when
        RequestException exception = assertThrows(RequestException.class, ()-> {
            pointService.chargePoint(1L,3000L); });
        // then
        assertThat(exception.getMessage()).isEqualTo("해당 유저를 찾을 수 없습니다.");
    }

}