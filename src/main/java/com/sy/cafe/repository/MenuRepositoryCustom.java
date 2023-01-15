package com.sy.cafe.repository;

import com.sy.cafe.dto.response.MenuResponseDto;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Long> popularMenus();

}
