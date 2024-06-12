package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.application.dto.ProjectSummaryDTO;
import com.goldengit.application.dto.ReleaseDTO;
import com.goldengit.application.service.ProjectOverviewService;
import com.goldengit.infra.config.WebConfig;
import com.goldengit.web.mapper.ProjectSummaryResponseMapper;
import com.goldengit.web.mapper.ReleaseResponseMapper;
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
        ProjectOverviewService.class,
        ProjectSummaryResponseMapper.class,
        ReleaseResponseMapper.class,
        ProjectOverviewController.class,
        ObjectMapper.class
})
@AutoConfigureMockMvc
public class ProjectOverviewControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private ProjectOverviewController projectOverviewController;

    @MockBean
    private ProjectOverviewService projectOverviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ProjectSummaryResponseMapper projectSummaryResponseMapper;

    @Autowired
    ReleaseResponseMapper releaseResponseMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectOverviewController)
                .build();
    }

    @Test
    public void shouldGetProjectSummary() throws Exception {
        String uuid = "sample";
        ProjectSummaryDTO projectSummaryDTO = ProjectSummaryDTO.builder().fullName("owner/repo").build();
        var projectSummaryResponse = projectSummaryResponseMapper.map(projectSummaryDTO);

        when(projectOverviewService.generateProjectSummary(uuid))
                .thenReturn(projectSummaryDTO);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/metrics/project-summary".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(projectSummaryResponse));
    }

    public void shouldGetRepositoryReleases() throws Exception {

        String uuid = "sample";
        var releases = List.of(ReleaseDTO.builder().name("v1").build());
        var releaseResponses = releaseResponseMapper.mapList(releases);

        when(projectOverviewService.findAllReleasesByRepo(uuid))
                .thenReturn(releases);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/overview/releases".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(releaseResponses));
    }
}
