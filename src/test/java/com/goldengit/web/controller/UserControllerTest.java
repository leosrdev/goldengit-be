package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.web.config.AppConfig;
import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.dto.UserResponse;
import com.goldengit.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AppConfig.class, UserController.class, ObjectMapper.class})
@AutoConfigureMockMvc
class UserControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;
    @MockBean
    private UserService userService;
    private UserRequest userRequest;
    private UserResponse userResponse;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();

        userRequest = UserRequest.builder()
                .name("John")
                .email("john@server.net")
                .password("secret")
                .build();

        userResponse = UserResponse.builder()
                .name("John")
                .email("john@server.net")
                .id("3838929")
                .build();
    }

    @Test
    void shouldCreateUser() throws Exception {
        when(userService.save(userRequest)).thenReturn(userResponse);
        String requestAsJson = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldFindById() throws Exception {

        when(userService.findById(userResponse.getId())).thenReturn(userResponse);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/users/" + userResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldListUsers() throws Exception {
        List<UserResponse> users = new ArrayList<>();
        users.add(userResponse);
        when(userService.findAll()).thenReturn(users);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteById() throws Exception {
        doNothing().when(userService).deleteById("1");
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/users/" + userResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }
}
