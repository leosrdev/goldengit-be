package com.goldengit.web.service;

import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.dto.UserResponse;
import com.goldengit.web.model.User;
import com.goldengit.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse save(UserRequest userRequest) {
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword()).build();

        User userCreated = userRepository.save(user);
        return UserResponse.builder()
                .id(userCreated.getId())
                .name(userCreated.getName())
                .email(userCreated.getEmail())
                .build();
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(user -> UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build()).toList();
    }

    public UserResponse findById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> UserResponse.builder()
                .id(value.getId())
                .name(value.getName())
                .email(value.getEmail()).build()).orElse(null);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}
