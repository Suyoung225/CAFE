package com.sy.cafe.repository;

import com.sy.cafe.config.TestConfig;
import com.sy.cafe.user.domain.PointHistory;
import com.sy.cafe.user.domain.PointType;
import com.sy.cafe.user.repository.PointRepository;
import com.sy.cafe.user.domain.User;
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
        PointHistory pointHistory2 = pointRepository.save(pointHistory);

        // then
        assertThat(pointHistory2.getPoint()).isEqualTo(5000L);
        assertThat(pointHistory2.getType()).isEqualTo(PointType.CHARGE);
        assertThat(pointHistory2.getUser().getNickname()).isEqualTo("sy");
    }


}