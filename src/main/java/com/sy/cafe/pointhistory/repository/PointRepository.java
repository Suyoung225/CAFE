package com.sy.cafe.pointhistory.repository;

import com.sy.cafe.pointhistory.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<PointHistory, Long> {
}
