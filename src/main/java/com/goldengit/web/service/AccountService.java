package com.goldengit.web.service;

import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.exception.AccountAlreadyExistsException;
import com.goldengit.web.exception.DisposableEmailException;
import com.zliio.disposable.Disposable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {
    private final UserService userService;
    private final Disposable disposable;

    public void register(UserRequest userRequest) throws AccountAlreadyExistsException, DisposableEmailException {
        if (!disposable.validate(userRequest.getEmail())) {
            throw new DisposableEmailException("Invalid domain");
        }

        if (userService.findByEmail(userRequest.getEmail()) == null) {
            userService.save(userRequest);
        } else {
            throw new AccountAlreadyExistsException("Account already exists");
        }
    }
}
