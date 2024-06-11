package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.infra.config.WebConfig;
import com.goldengit.web.model.UserRequest;
import com.goldengit.domain.exception.AccountAlreadyExistsException;
import com.goldengit.domain.exception.InvalidEmailDomainException;
import com.goldengit.application.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = {
        WebConfig.class,
        AccountService.class,
        AccountController.class,
        ObjectMapper.class
})
@AutoConfigureMockMvc
public class AccountControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private AccountController accountController;

    @MockBean
    private AccountService accountService;

    private UserRequest userRequest;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .build();

        userRequest = UserRequest.builder()
                .name("John")
                .email("john@server.net")
                .password("secret")
                .build();
    }

    @Test
    void shouldRegisterNewAccount() throws Exception {
        Mockito.doNothing().when(accountService).register(userRequest);
        String requestAsJson = objectMapper.writeValueAsString(userRequest);

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Disabled
    @Test
    void shouldReturnConflictWhenAccountAlreadyExists() throws Exception {
        Mockito.doThrow(new AccountAlreadyExistsException(""))
                .when(accountService)
                .register(userRequest);
        String requestAsJson = objectMapper.writeValueAsString(userRequest);
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Disabled
    @Test
    void shouldReturnBadRequestForDisposableEmail() throws Exception {
        Mockito.doThrow(new InvalidEmailDomainException("Invalid domain"))
                .when(accountService)
                .register(userRequest);
        String requestAsJson = objectMapper.writeValueAsString(userRequest);
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnBadRequestForInvalidEmail() throws Exception {
        String requestAsJson = objectMapper.writeValueAsString(userRequest.toBuilder().email("invalid").build());
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnBadRequestForInvalidName() throws Exception {
        String requestAsJson = objectMapper.writeValueAsString(userRequest.toBuilder().name("").build());
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnBadRequestForInvalidPassword() throws Exception {
        String requestAsJson = objectMapper.writeValueAsString(userRequest.toBuilder().password("").build());
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson)
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldActivateAccount() throws Exception {
        Mockito.doNothing().when(accountService).activate("foobar");
        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/accounts/activate?t=foobar")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
