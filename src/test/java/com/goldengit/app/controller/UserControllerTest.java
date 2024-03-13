package com.goldengit.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.app.dto.UserRequest;
import com.goldengit.app.dto.UserResponse;
import com.goldengit.app.repository.UserRepository;
import com.goldengit.app.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest extends BaseControllerTest {

    @Test
    void shouldCreateUser() throws Exception {
        UserRequest userRequest = getUserRequest();
        String requestAsJson = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldFindById() throws Exception {
        UserRequest userRequest = getUserRequest();
        UserResponse userResponse = userService.createUser(userRequest);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/users/" + userResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldListUsers() throws Exception {
        UserRequest userRequest = getUserRequest();
        userService.createUser(userRequest);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    private UserRequest getUserRequest() {
        return UserRequest.builder()
                .name("John")
                .email("john@server.net")
                .password("secret")
                .build();
    }
}
