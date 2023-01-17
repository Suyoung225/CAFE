package com.sy.cafe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sy.cafe.domain.QOrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MenuRepositoryImpl implements MenuRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QOrderItem orderItem = QOrderItem.orderItem;

    @Override
    public List<Long> popularMenus() {
        LocalDate weekBefore = LocalDate.now().minusDays(7);
        LocalDate yesterday = LocalDate.now();

        return queryFactory.select(orderItem.menuId)
                .from(orderItem)
                .where(orderItem.createdTime.between(weekBefore.atStartOfDay(), yesterday.atStartOfDay()))
                .groupBy(orderItem.menuId)
                .orderBy(orderItem.number.sum().desc())
                .limit(3)
                .fetch();
    }
}
