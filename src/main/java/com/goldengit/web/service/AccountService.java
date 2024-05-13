package com.goldengit.web.service;

import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.exception.AccountAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {
    private final UserService userService;

    public void register(UserRequest userRequest) throws AccountAlreadyExistsException {
        if (userService.findByEmail(userRequest.getEmail()) == null) {
            userService.save(userRequest);
        } else {
            throw new AccountAlreadyExistsException("Account already exists");
        }
    }
}
