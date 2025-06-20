package com.goldengit.web.controller;

import com.goldengit.application.service.AccountService;
import com.goldengit.domain.exception.AccountAlreadyExistsException;
import com.goldengit.domain.exception.InvalidEmailDomainException;
import com.goldengit.web.model.UserRequest;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            throws AccountAlreadyExistsException, InvalidEmailDomainException {
        if (bucket.tryConsume(1)) {
            accountService.register(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.warn(String.format("Too many requests, IP %s", httpRequest.getRemoteAddr()));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    @GetMapping(path = "/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activate(@RequestParam(value = "t") @Size(min = 1, max = 128) String token) throws BadRequestException {
        accountService.activate(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
