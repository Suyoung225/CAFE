package com.sy.cafe.dto;

import com.sy.cafe.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Long id;
    String nickname;
    Long point;

    public UserDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.point = user.getPoint();
    }
}
