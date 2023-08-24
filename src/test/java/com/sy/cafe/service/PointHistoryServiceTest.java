package com.sy.cafe.service;

import com.sy.cafe.user.controller.dto.PointHistoryResponseDto;
import com.sy.cafe.user.domain.PointHistory;
import com.sy.cafe.user.domain.PointType;
import com.sy.cafe.user.domain.User;
import com.sy.cafe.user.repository.PointRepository;
import com.sy.cafe.user.repository.UserRepository;
import com.sy.cafe.user.service.PointHistoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceTest {

    @InjectMocks
    private PointHistoryServiceImpl pointService;
    @Mock
    private PointRepository pointRepository;

    @Mock
    private UserRepository userRepository;

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


    @Test
    @DisplayName("포인트 이력 타입벽 조회")
    void showPointHistoryByTpe(){
        // given
        User user = User.builder().id(1L).point(10000L).nickname("sy").build();

        Pageable pageable = Pageable.ofSize(5);

        PointHistory pointHistory1 = PointHistory.builder().id(1L).point(5000L).type(PointType.CHARGE).build();
        PointHistory pointHistory2 = PointHistory.builder().id(1L).point(1000L).type(PointType.CHARGE).build();
        List<PointHistory> pointHistoryList = new ArrayList<>();
        pointHistoryList.add(pointHistory1);
        pointHistoryList.add(pointHistory2);

        Page<PointHistory> pointHistoryPage = new PageImpl<>(pointHistoryList);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(pointRepository.findByUserAndTypeOrderByCreatedTimeDesc(user, PointType.CHARGE, pageable)).thenReturn(pointHistoryPage);

        // when
        Page<PointHistoryResponseDto> pointHistoryResponseDtos = pointService.showPointHistoryByType(1L, "CHARGE", pageable);

        // then
        assertThat(pointHistoryResponseDtos.getTotalElements()).isEqualTo(2L);
    }

    @Test
    @DisplayName("포인트 이력 조회")
    void showPointHistory(){
        // given
        User user = User.builder().id(1L).point(10000L).nickname("sy").build();

        Pageable pageable = Pageable.ofSize(5);

        PointHistory pointHistory1 = PointHistory.builder().id(1L).point(5000L).type(PointType.CHARGE).build();
        PointHistory pointHistory2 = PointHistory.builder().id(1L).point(1000L).type(PointType.PAYMENT).build();
        List<PointHistory> pointHistoryList = new ArrayList<>();
        pointHistoryList.add(pointHistory1);
        pointHistoryList.add(pointHistory2);

        Page<PointHistory> pointHistoryPage = new PageImpl<>(pointHistoryList);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(pointRepository.findByUserOrderByCreatedTimeDesc(user, pageable)).thenReturn(pointHistoryPage);

        // when
        Page<PointHistoryResponseDto> pointHistoryResponseDtos = pointService.showPointHistoryByType(1L, null, pageable);

        // then
        assertThat(pointHistoryResponseDtos.getTotalElements()).isEqualTo(2L);
    }
}
