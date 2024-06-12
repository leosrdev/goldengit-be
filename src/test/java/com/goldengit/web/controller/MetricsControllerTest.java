package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.application.dto.IssueSummaryDTO;
import com.goldengit.application.dto.PullRequestSummaryDTO;
import com.goldengit.application.dto.WeekOfCommitDTO;
import com.goldengit.application.service.MetricsService;
import com.goldengit.infra.config.WebConfig;
import com.goldengit.web.mapper.IssueSummaryResponseMapper;
import com.goldengit.web.mapper.PullRequestSummaryResponseMapper;
import com.goldengit.web.mapper.WeekOfCommitResponseMapper;
import org.apache.coyote.BadRequestException;
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
        MetricsService.class,
        WeekOfCommitResponseMapper.class,
        PullRequestSummaryResponseMapper.class,
        IssueSummaryResponseMapper.class,
        MetricsController.class,
        ObjectMapper.class
})
@AutoConfigureMockMvc
public class MetricsControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private MetricsController metricsController;

    @MockBean
    private MetricsService metricsService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(metricsController)
                .build();
    }

    @Test
    void shouldGetCommitActivityByWeek() throws Exception {
        String uuid = "sample";
        WeekOfCommitDTO weekResponse = WeekOfCommitDTO.builder().week(1).total(1).build();

        when(metricsService.getCommitActivityByWeek(uuid))
                .thenReturn(List.of(weekResponse));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/metrics/commit-activity".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(weekResponse)));
    }

    @Test
    void shouldThrowExceptionForGetCommitActivityByWeek() throws Exception {
        String uuid = "sample";
        when(metricsService.getCommitActivityByWeek(uuid))
                .thenThrow(new BadRequestException());

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/metrics/commit-activity".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldGetAccumulatedCommitsByWeek() throws Exception {
        String uuid = "sample";
        WeekOfCommitDTO weekResponse = WeekOfCommitDTO.builder().week(1).total(1).build();

        when(metricsService.getAccumulatedCommitsByWeek(uuid))
                .thenReturn(List.of(weekResponse));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/metrics/accumulated-commits".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(weekResponse)));
    }

    @Test
    void shouldThrowExceptionForGetAccumulatedCommitsByWeek() throws Exception {
        String uuid = "sample";
        when(metricsService.getAccumulatedCommitsByWeek(uuid))
                .thenThrow(new BadRequestException());

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/metrics/accumulated-commits".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldGetPullRequestsByDate() throws Exception {
        String uuid = "sample";
        PullRequestSummaryDTO pullRequestSummaryDTO = PullRequestSummaryDTO.builder()
                .date("20020-01-01")
                .total(10L).
                build();

        when(metricsService.getPullRequestsSummary(uuid))
                .thenReturn(List.of(pullRequestSummaryDTO));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/metrics/pull-requests-summary".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(pullRequestSummaryDTO)));
    }

    @Test
    void shouldThrowExceptionForGetPullRequestsByDate() throws Exception {
        String uuid = "sample";
        when(metricsService.getPullRequestsSummary(uuid))
                .thenThrow(new BadRequestException());

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/metrics/pull-requests-summary".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldGetIssuesByDate() throws Exception {
        String uuid = "sample";
        IssueSummaryDTO issueSummaryDTO = IssueSummaryDTO.builder()
                .date("20020-01-01")
                .total(10L).
                build();

        when(metricsService.getIssuesSummary(uuid))
                .thenReturn(List.of(issueSummaryDTO));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/metrics/issues-summary".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(issueSummaryDTO)));
    }

    @Test
    void shouldThrowExceptionForGetIssuesByDate() throws Exception {
        String uuid = "sample";
        when(metricsService.getIssuesSummary(uuid))
                .thenThrow(new BadRequestException());

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/metrics/issues-summary".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
