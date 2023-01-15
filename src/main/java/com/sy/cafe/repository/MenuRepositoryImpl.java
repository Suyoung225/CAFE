package com.sy.cafe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sy.cafe.domain.QMenuOrders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MenuRepositoryImpl implements MenuRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QMenuOrders menuOrders = QMenuOrders.menuOrders;

    @Override
    public List<Long> popularMenus() {
        LocalDate weekBefore = LocalDate.now().minusDays(7);
        LocalDate yesterday = LocalDate.now();

        return queryFactory.select(menuOrders.id)
                .from(menuOrders)
                .where(menuOrders.createdTime.between(weekBefore.atStartOfDay(), yesterday.atStartOfDay()))
                .groupBy(menuOrders.menu)
                .orderBy(menuOrders.number.sum().desc())
                .limit(3)
                .fetch();
    }
}
