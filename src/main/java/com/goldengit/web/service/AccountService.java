package com.goldengit.web.service;

import com.goldengit.web.dto.EmailMessage;
import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.exception.AccountAlreadyExistsException;
import com.goldengit.web.exception.DisposableEmailException;
import com.zliio.disposable.Disposable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

import static com.goldengit.web.config.RedisConfig.ACTIVATION_KEYS;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private final UserService userService;
    private final Disposable disposable;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailProducerService emailProducerService;

    public void register(UserRequest userRequest) throws AccountAlreadyExistsException, DisposableEmailException {
        UserRequest request = userRequest.toBuilder().email(userRequest.getEmail().toLowerCase()).build();
        if (isDisposableEmail(request.getEmail())) {
            throw new DisposableEmailException("Invalid domain");
        }

        if (accountDoesNotExist(request.getEmail())) {
            userService.save(request);
            sendEmailToActivateAccount(request);
        } else {
            throw new AccountAlreadyExistsException("Account already exists");
        }
    }

    private void sendEmailToActivateAccount(UserRequest request) {
        String uuid = generateUUID();
        saveEmailActivationTokenIntoCache(request.getEmail(), uuid);
        emailProducerService.publishEmailMessage(new EmailMessage(request.getName(), request.getEmail(), uuid));
    }

    private void saveEmailActivationTokenIntoCache(String email, String uuid) {
        redisTemplate.opsForValue().set(String.format("%s::%s", ACTIVATION_KEYS, uuid), email);
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
