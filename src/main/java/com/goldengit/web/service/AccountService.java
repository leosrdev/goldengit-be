package com.goldengit.web.service;

import com.goldengit.web.dto.UserRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {
    private final UserService userService;

    public void register(UserRequest userRequest) {
        userService.save(userRequest);
    }
}
