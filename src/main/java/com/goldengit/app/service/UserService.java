package com.goldengit.app.service;

import com.goldengit.app.dto.UserRequest;
import com.goldengit.app.model.User;
import com.goldengit.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(UserRequest userRequest) {
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword()).build();

        userRepository.save(user);
    }
}
