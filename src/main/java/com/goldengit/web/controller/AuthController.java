package com.goldengit.web.controller;

import com.goldengit.application.service.AuthService;
import com.goldengit.web.model.AuthRequest;
import com.goldengit.web.model.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest authRequest) {
        try {
            String token = authService.generateToken(authRequest.getVisitorId());
            return ResponseEntity.ok(AuthResponse.builder().token(token).build());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
