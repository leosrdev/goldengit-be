package com.goldengit.web.controller;

import com.goldengit.web.config.AppConfig;
import com.goldengit.restclient.service.GitService;
import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.RepoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AppConfig.class, RepoController.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RepoControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private GitService gitService;

    @Autowired
    private RepoController repoController;

    private final List<RepoResponse> repoResponse = new ArrayList<>();
    private final List<PullRequestResponse> pullRequestResponse = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(repoController)
                .build();

        repoResponse.add(RepoResponse.builder()
                .fullName("spring/spring-boot")
                .openIssues(5)
                .watchers(100)
                .forks(50)
                .stars(1000)
                .description("Project beta")
                .defaultBranch("master")
                .build());

        pullRequestResponse.add(PullRequestResponse.builder()
                .id(1)
                .state("open")
                .createdAt("2020-01-30")
                .title("Fixing bugs")
                .number(1900)
                .build());
    }

    @Test
    void shouldListPullRequests() throws Exception {
        when(gitService.findRepoByQuery("spring")).thenReturn(repoResponse);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(String.format("/api/v1/repos/search?q=%s", "spring"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldSearchRepositories() throws Exception {
        when(gitService.findPullRequestByRepoName("spring", "spring-boot")).thenReturn(pullRequestResponse);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(String.format("/api/v1/repos/%s/%s/pulls", "spring", "spring-boot"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
