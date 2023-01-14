package com.sy.cafe.repository;

import com.sy.cafe.domain.Menu;
import com.sy.cafe.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
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

}