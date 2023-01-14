package com.sy.cafe.repository;

import com.sy.cafe.domain.Point;
import com.sy.cafe.domain.PointType;
import com.sy.cafe.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PointRepositoryTest {

    @Autowired
    PointRepository pointRepository;

    @Test
    @DisplayName("포인트 충전")
    void registerUser(){
        // given
        User user = User.builder().id(1L).point(2000L).nickname("sy").build();
        Point point = Point.builder()
                .type(PointType.CHARGE)
                .user(user)
                .point(5000L)
                .build();

        // when
        pointRepository.save(point);

        // then
        assertThat(point.getPoint()).isEqualTo(5000L);
        assertThat(point.getType()).isEqualTo(PointType.CHARGE);
        assertThat(point.getUser().getNickname()).isEqualTo("sy");
    }
}