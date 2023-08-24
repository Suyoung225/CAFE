package com.sy.cafe.user.controller.dto;

import com.sy.cafe.user.domain.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PointHistoryResponseDto {
    Long point;
    String type;
    LocalDateTime createdTime;

    public static PointHistoryResponseDto from(PointHistory pointHistory){
        return new PointHistoryResponseDto(
                pointHistory.getPoint(),
                pointHistory.getType().toString(),
                pointHistory.getCreatedTime());
    }
}
