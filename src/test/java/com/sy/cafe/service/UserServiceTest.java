package com.sy.cafe.service;

import com.sy.cafe.domain.Menu;
import com.sy.cafe.domain.User;
import com.sy.cafe.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 추가")
    void register() {
        // given
        User user = new User("수영");
        when(userRepository.save(any())).thenReturn(user);

        // when
        String nickname = userService.register("수영").getNickname();
        Long point = userService.register("수영").getPoint();

        // then
        assertThat(nickname).isEqualTo("수영");
        assertThat(point).isEqualTo(0L);
    }
}