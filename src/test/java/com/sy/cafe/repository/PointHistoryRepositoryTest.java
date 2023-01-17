package com.sy.cafe.repository;

import com.sy.cafe.config.TestConfig;
import com.sy.cafe.domain.PointHistory;
import com.sy.cafe.domain.PointType;
import com.sy.cafe.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import({TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PointHistoryRepositoryTest {

    @Autowired
    PointRepository pointRepository;

    @Test
    @DisplayName("포인트 충전")
    void registerUser(){
        // given
        User user = User.builder().id(1L).point(2000L).nickname("sy").build();
        PointHistory pointHistory = PointHistory.builder()
                .type(PointType.CHARGE)
                .user(user)
                .point(5000L)
                .build();

        // when
        pointRepository.save(pointHistory);

        // then
        assertThat(pointHistory.getPoint()).isEqualTo(5000L);
        assertThat(pointHistory.getType()).isEqualTo(PointType.CHARGE);
        assertThat(pointHistory.getUser().getNickname()).isEqualTo("sy");
    }
}