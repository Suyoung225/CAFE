package com.sy.cafe.service;

import com.sy.cafe.domain.User;
import com.sy.cafe.repository.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceTest {

    @InjectMocks
    private PointService pointService;
    @Mock
    private PointRepository pointRepository;

    @Test
    @DisplayName("포인트 충전")
    void charge(){
        // given
        User user = User.builder().id(1L).point(1000L).nickname("sy").build();
        // when
        pointService.chargePoint(5000L, user);
        // then
        verify(pointRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("포인트 사용")
    void use(){
        // given
        User user = User.builder().id(1L).point(10000L).nickname("sy").build();
        // when
        pointService.usePoint(5000L, user);
        // then
        verify(pointRepository, times(1)).save(any());
    }




}