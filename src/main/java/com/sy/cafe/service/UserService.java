package com.sy.cafe.service;

import com.sy.cafe.domain.User;
import com.sy.cafe.dto.response.UserResponseDto;
import com.sy.cafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto register(String nickname) {
        User user = new User(nickname);
        userRepository.save(user);
        return new UserResponseDto(user);
    }

}
