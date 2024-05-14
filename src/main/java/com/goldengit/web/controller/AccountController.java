package com.goldengit.web.controller;

import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.exception.AccountAlreadyExistsException;
import com.goldengit.web.exception.DisposableEmailException;
import com.goldengit.web.service.AccountService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/accounts")
@Slf4j
@Validated
public class AccountController {
    private final AccountService accountService;
    private final Bucket bucket;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
        bucket = Bucket.builder()
                .addLimit(limit -> limit.capacity(20).refillGreedy(5, Duration.ofMinutes(1)))
                .build();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest, HttpServletRequest httpRequest)
            throws AccountAlreadyExistsException, DisposableEmailException {
        if (bucket.tryConsume(1)) {
            accountService.register(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.warn(String.format("Too many requests, IP %s", httpRequest.getRemoteAddr()));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }
}
