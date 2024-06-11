package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.infra.config.WebConfig;
import com.goldengit.web.model.UserRequest;
import com.goldengit.web.model.UserResponse;
import com.goldengit.application.service.UserService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest(classes = {
        WebConfig.class,
        UserController.class,
        ObjectMapper.class
})
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
    }

    @Test
    void shouldCreateUser() throws Exception {
        when(userService.save(userRequest)).thenReturn(userResponse);
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
        when(userService.findById(1)).thenReturn(userResponse);
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
        when(userService.findAll()).thenReturn(List.of(userResponse));
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
        doNothing().when(userService).deleteById(1);
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
