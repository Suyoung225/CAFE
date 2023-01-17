package com.sy.cafe.repository;

import com.sy.cafe.config.TestConfig;
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
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원 추가")
    void registerUser(){
        // given
        User user = User.builder().id(1L).point(0L).nickname("sy").build();

        // when
        User user2 = userRepository.save(user);

        // then
        assertThat(user2.getId()).isEqualTo(user.getId());
        assertThat(user2.getNickname()).isEqualTo(user.getNickname());
    }

    @Test
    @DisplayName("포인트 충전")
    void chargePoint(){
        // given
        User user = User.builder().id(1L).point(1000L).nickname("sy").build();
        user.chargePoint(5000L);

        // when
        User user2 = userRepository.save(user);

        // then
        assertThat(user2.getPoint()).isEqualTo(6000L);
    }


}