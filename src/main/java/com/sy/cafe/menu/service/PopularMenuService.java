package com.sy.cafe.menu.service;

import com.sy.cafe.menu.controller.dto.PopularMenuDto;

import java.util.List;

public interface PopularMenuService {
    List<PopularMenuDto> top3PopularMenusIn7Days();

}
