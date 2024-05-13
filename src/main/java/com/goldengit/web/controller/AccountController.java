package com.goldengit.web.controller;

import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@AllArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest) {
        try {
            accountService.register(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
