package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.restclient.service.GitService;
import com.goldengit.web.config.AppConfig;
import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.RepoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = {AppConfig.class, RepoController.class, ObjectMapper.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RepoControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private GitService gitService;

    @Autowired
    private RepoController repoController;

    private RepoResponse repoResponse;

    @Autowired
    private ObjectMapper objectMapper;
    private PullRequestResponse pullRequestResponse;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(repoController)
                .build();

        repoResponse = RepoResponse.builder()
                .fullName("spring/spring-boot")
                .openIssues(5)
                .watchers(100)
                .forks(50)
                .stars(1000)
                .description("Project beta")
                .defaultBranch("master")
                .build();

        pullRequestResponse = PullRequestResponse.builder()
                .id(1)
                .state("open")
                .createdAt("2020-01-30")
                .title("Fixing bugs")
                .number(1900)
                .build();
    }

    @Test
    void shouldListPullRequests() throws Exception {
        when(gitService.findRepoByQuery("spring")).thenReturn(List.of(repoResponse));

        MockHttpServletResponse response = mockMvc.perform(get(String.format("/api/v1/repos/search?q=%s", "spring"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(Arrays.asList(repoResponse)));

    }

    @Test
    void shouldSearchRepositories() throws Exception {
        when(gitService.findPullRequestByRepoName("spring", "spring-boot")).thenReturn(List.of(pullRequestResponse));

        MockHttpServletResponse response = mockMvc.perform(
                        get(String.format("/api/v1/repos/%s/%s/pulls", "spring", "spring-boot"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(Arrays.asList(pullRequestResponse)));

    }
}
