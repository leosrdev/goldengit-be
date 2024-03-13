package com.goldengit.app.controller;

import com.goldengit.app.dto.UserRequest;
import com.goldengit.app.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
