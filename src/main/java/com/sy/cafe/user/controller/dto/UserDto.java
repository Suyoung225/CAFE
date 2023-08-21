package com.sy.cafe.user.controller.dto;

import com.sy.cafe.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDto {
    Long userId;
    String nickname;
    Long point;

    public static UserDto from(User user){
        return new UserDto(user.getId(), user.getNickname(), user.getPoint());
    }
}
