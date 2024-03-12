package com.goldengit.app.service;

import com.goldengit.app.dto.UserRequest;
import com.goldengit.app.dto.UserResponse;
import com.goldengit.app.model.User;
import com.goldengit.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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

    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream().map(user -> UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build()).toList();
    }

    public UserResponse getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> UserResponse.builder()
                .id(value.getId())
                .name(value.getName())
                .email(value.getEmail()).build()).orElse(null);
    }
}
