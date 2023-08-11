package com.sy.cafe.menu.repository;

import com.sy.cafe.menu.controller.dto.PopularMenuDto;

import java.util.List;

public interface MenuRepositoryCustom {
    List<PopularMenuDto> popularMenus();

}
