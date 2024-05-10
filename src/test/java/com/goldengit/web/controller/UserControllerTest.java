package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.web.config.AppConfig;
import com.goldengit.web.dto.UserRequest;
import com.goldengit.web.dto.UserResponse;
import com.goldengit.web.model.User;
import com.goldengit.web.repository.UserRepository;
import com.goldengit.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = {AppConfig.class, UserController.class, UserService.class, ObjectMapper.class})
@AutoConfigureMockMvc
class UserControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;
    @MockBean
    private UserRepository userRepository;
    private UserRequest userRequest;
    private UserResponse userResponse;
    private User user;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
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
                .id(1)
                .build();

        user = User.builder().name("John")
                .email("john@server.net")
                .password("secret")
                .build();
    }

    @Test
    void shouldCreateUser() throws Exception {
        when(userRepository.save(user)).thenReturn(user.toBuilder().id(1).build());
        String requestAsJson = objectMapper.writeValueAsString(userRequest);

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(userResponse));
    }

    @Test
    void shouldFindById() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(user.toBuilder().id(1).build()));
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(userResponse));
    }

    @Test
    void shouldListUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(user.toBuilder().id(1).build()));
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(List.of(userResponse)));
    }

    @Test
    void shouldDeleteById() throws Exception {
        doNothing().when(userRepository).deleteById(1);
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
