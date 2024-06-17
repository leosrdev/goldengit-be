package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.application.dto.ProjectDTO;
import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.service.ProjectService;
import com.goldengit.infra.config.WebConfig;
import com.goldengit.web.mapper.ProjectResponseMapper;
import com.goldengit.web.mapper.PullRequestResponseMapper;
import com.goldengit.web.model.ProjectResponse;
import com.goldengit.web.model.PullRequestResponse;
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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = {
        WebConfig.class,
        ProjectService.class,
        ProjectResponseMapper.class,
        PullRequestResponseMapper.class,
        ProjectController.class,
        ObjectMapper.class
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private ProjectService projectService;

    @Autowired
    private ProjectController projectController;

    private ProjectResponse projectResponse;

    private ProjectDTO projectDTO;
    private PullRequestDTO pullRequestDTO;

    @Autowired
    private ObjectMapper objectMapper;
    private PullRequestResponse pullRequestResponse;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectController)
                .build();

        projectDTO = ProjectDTO.builder()
                .fullName("spring/spring-boot")
                .openIssues(5)
                .watchers(100)
                .forks(50)
                .stars(1000)
                .description("Project beta")
                .defaultBranch("master")
                .build();

        projectResponse = ProjectResponse.builder()
                .fullName("spring/spring-boot")
                .openIssues(5)
                .watchers(100)
                .forks(50)
                .stars(1000)
                .description("Project beta")
                .defaultBranch("master")
                .build();

        pullRequestDTO = PullRequestDTO.builder()
                .id(1)
                .state("open")
                .createdAt("2020-01-30")
                .title("Fixing bugs")
                .number(1900)
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
        when(projectService.findRepoByQuery("spring")).thenReturn(List.of(projectDTO));

        MockHttpServletResponse response = mockMvc.perform(get(String.format("/api/v1/repos/search?q=%s", "spring"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(Collections.singletonList(projectResponse)));

    }

    @Test
    void shouldListPopularRepos() throws Exception {
        when(projectService.listPopularProjects()).thenReturn(List.of(projectDTO));

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/repos/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(Collections.singletonList(projectDTO)));

    }
}
