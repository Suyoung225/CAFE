package com.sy.cafe.menu.controller.dto;

import com.sy.cafe.menu.domain.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MenuDto {
    private Long id;

    private String name;

    private Long price;

    public MenuDto(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public MenuDto(Menu menu){
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
    }
}
