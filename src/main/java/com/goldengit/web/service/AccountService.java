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

import static com.goldengit.web.config.RedisConfig.ACTIVATION_KEYS;

@Service
@AllArgsConstructor
public class AccountService {
    private final UserService userService;
    private final Disposable disposable;
    private final RedisTemplate<String, String> redisTemplate;

    public void register(UserRequest userRequest) throws AccountAlreadyExistsException, DisposableEmailException {
        UserRequest request = userRequest.toBuilder().email(userRequest.getEmail().toLowerCase()).build();
        if (isDisposableEmail(request.getEmail())) {
            throw new DisposableEmailException("Invalid domain");
        }

        if (accountDoesNotExist(request.getEmail())) {
            userService.save(request);
            createAccountActivationKey(request.getEmail());
        } else {
            throw new AccountAlreadyExistsException("Account already exists");
        }
    }

    private void createAccountActivationKey(String email) {
        redisTemplate.opsForValue().set(String.format("%s::%s", ACTIVATION_KEYS, generateUUID()), email);
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
