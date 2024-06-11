package com.goldengit.application.service;

import com.goldengit.domain.exception.AccountAlreadyExistsException;
import com.goldengit.domain.exception.InvalidEmailDomainException;
import com.goldengit.web.model.EmailMessage;
import com.goldengit.web.model.UserRequest;
import com.zliio.disposable.Disposable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private final UserService userService;
    private final Disposable disposable;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailProducerService emailProducerService;

    public void register(UserRequest userRequest) throws AccountAlreadyExistsException, InvalidEmailDomainException {
        UserRequest request = userRequest.toBuilder().email(userRequest.getEmail().toLowerCase()).build();
        if (isDisposableEmail(request.getEmail())) {
            throw new InvalidEmailDomainException("Invalid domain");
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
        redisTemplate.opsForValue().set(uuid, email, 3, TimeUnit.HOURS);
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

    public void activate(String token) throws BadRequestException {
        String email = redisTemplate.opsForValue().get(token);
        if (email != null) {
            userService.activateAccount(email);
            redisTemplate.delete(token);
        } else {
            throw new BadRequestException("Invalid token");
        }
    }
}
