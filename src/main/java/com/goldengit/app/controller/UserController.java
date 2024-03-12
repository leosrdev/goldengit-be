package com.goldengit.app.controller;

import com.goldengit.app.dto.UserRequest;
import com.goldengit.app.dto.UserResponse;
import com.goldengit.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
        public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            userService.createUser(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("success");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("We could not process your request, please try again later.");
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.listUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        UserResponse userResponse = userService.getUserById(id);
        if (userResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
