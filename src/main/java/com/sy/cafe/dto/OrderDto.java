package com.sy.cafe.dto;

import com.sy.cafe.domain.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    Long menuId;
    int number;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrderDto))
            return false;
        OrderDto dto = (OrderDto)o;
        return Objects.equals(menuId, dto.menuId) &&
                Objects.equals(number, dto.number);
    }
}
