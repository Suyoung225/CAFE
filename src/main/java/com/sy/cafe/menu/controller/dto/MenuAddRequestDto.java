package com.sy.cafe.menu.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MenuAddRequestDto {
    @Size(max = 30, message = "이름은 30글자 이내로 설정해주세요.")
    private String name;

    @PositiveOrZero(message = "음수는 입력할 수 없습니다.")
    private Long price;

    public MenuAddRequestDto(String name, Long price) {
        this.name = name;
        this.price = price;
    }
}
