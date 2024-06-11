package com.goldengit.application.service;

import com.goldengit.domain.model.User;
import com.goldengit.infra.db.UserRepository;
import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @CachePut(cacheNames = "users", key = "#userRequest.email")
    public UserResponse save(UserRequest userRequest) {
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail().toLowerCase())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();

        User userCreated = userRepository.save(user);
        return UserResponse.builder()
                .id(userCreated.getId())
                .name(userCreated.getName())
                .email(userCreated.getEmail())
                .build();
    }

    public void activateAccount(String email) {
        userRepository.activateAccount(email);
    }

    public List<UserResponse> findAll() {
        var users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(user ->
                userResponses.add(UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail()).build())
        );
        return userResponses;
    }

    public UserResponse findById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> UserResponse.builder()
                .id(value.getId())
                .name(value.getName())
                .email(value.getEmail()).build()).orElse(null);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Cacheable("users")
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .flatMap(value ->
                        Optional.of(UserResponse.builder()
                                .id(value.getId())
                                .name(value.getName())
                                .email(value.getEmail()).build()));
    }
}
