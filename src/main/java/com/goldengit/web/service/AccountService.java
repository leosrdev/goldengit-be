package com.goldengit.web.service;

import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.exception.AccountAlreadyExistsException;
import com.goldengit.web.exception.DisposableEmailException;
import com.zliio.disposable.Disposable;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {
    private final UserService userService;
    private final Disposable disposable;
    private final RedisTemplate<String, String> redisTemplate;

    public void register(UserRequest userRequest) throws AccountAlreadyExistsException, DisposableEmailException {
        if (isDisposableEmail(userRequest.getEmail())) {
            throw new DisposableEmailException("Invalid domain");
        }

        if (accountDoesNotExist(userRequest.getEmail())) {
            userService.save(userRequest);
            saveKeyForAccountActivation(userRequest);
        } else {
            throw new AccountAlreadyExistsException("Account already exists");
        }
    }

    private void saveKeyForAccountActivation(UserRequest userRequest) {
        redisTemplate.opsForValue().set(generateUUID(), userRequest.getEmail());
    }

    private boolean accountDoesNotExist(String email) {
        return userService.findByEmail(email).isEmpty();
    }

    private boolean isDisposableEmail(String email) {
        return !disposable.validate(email);
    }

    private String generateUUID() {
        Random random = new Random();
        UUID uuid = new UUID(random.nextLong(), random.nextLong());
        return uuid.toString();
    }
}
