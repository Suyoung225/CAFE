package com.sy.cafe.menu.controller.dto;

import com.sy.cafe.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MenuDto {
    private Long id;

    private String name;

    private Long price;

    public static MenuDto from(Menu menu){
        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice());
    }
}
