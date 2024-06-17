package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.application.dto.IssueDTO;
import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.service.ProjectService;
import com.goldengit.infra.config.WebConfig;
import com.goldengit.web.mapper.IssueResponseMapper;
import com.goldengit.web.mapper.PullRequestResponseMapper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = {
        WebConfig.class,
        ProjectService.class,
        PullRequestResponseMapper.class,
        IssueResponseMapper.class,
        WorkInProgressController.class,
        ObjectMapper.class
})
@AutoConfigureMockMvc
public class WorkInProgressControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WorkInProgressController workInProgressController;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PullRequestResponseMapper pullRequestResponseMapper;

    @Autowired
    private IssueResponseMapper issueResponseMapper;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(workInProgressController)
                .build();
    }

    @Test
    public void shouldFindLatestPullRequestByRepoUuid() throws Exception {
        String uuid = "sample";
        var pullRequestDTO = PullRequestDTO.builder().id(1).number(10).build();
        var pullRequestResponse = pullRequestResponseMapper.map(pullRequestDTO);

        when(projectService.findLatestPullRequestByRepoUuid(uuid))
                .thenReturn(List.of(pullRequestDTO));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/pulls".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(pullRequestResponse)));
    }

    @Test
    public void shouldFindLatestIssues() throws Exception {
        String uuid = "sample";
        var issueDTO = IssueDTO.builder().id(1).number(10).build();
        var issueResponse = issueResponseMapper.map(issueDTO);

        when(projectService.findLatestIssues(uuid))
                .thenReturn(List.of(issueDTO));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/issues".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(issueResponse)));
    }


}
