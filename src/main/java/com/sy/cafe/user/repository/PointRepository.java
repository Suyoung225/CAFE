package com.sy.cafe.user.repository;

import com.sy.cafe.user.domain.PointHistory;
import com.sy.cafe.user.domain.PointType;
import com.sy.cafe.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<PointHistory, Long> {
    Page<PointHistory> findByUserOrderByCreatedTimeDesc(User user, Pageable pageable);
    Page<PointHistory> findByUserAndTypeOrderByCreatedTimeDesc(User user, PointType pointType, Pageable pageable);
}
