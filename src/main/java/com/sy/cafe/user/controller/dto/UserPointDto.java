package com.sy.cafe.user.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserPointDto {
    Long userId;
    Long point;

    public UserPointDto(Long id, Long point) {
        this.userId = id;
        this.point = point;
    }
}
