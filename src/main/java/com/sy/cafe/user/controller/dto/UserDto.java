package com.sy.cafe.user.controller.dto;

import com.sy.cafe.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    Long userId;
    String nickname;
    Long point;

    public UserDto(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.point = user.getPoint();
    }
}
