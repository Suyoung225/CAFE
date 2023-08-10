package com.sy.cafe.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointChargeRequestDto {
    Long userId;
    Long point;
}
