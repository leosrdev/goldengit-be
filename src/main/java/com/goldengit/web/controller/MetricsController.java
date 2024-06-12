package com.goldengit.web.controller;

import com.goldengit.application.service.MetricsService;
import com.goldengit.web.mapper.IssueSummaryResponseMapper;
import com.goldengit.web.mapper.PullRequestSummaryResponseMapper;
import com.goldengit.web.mapper.WeekOfCommitResponseMapper;
import com.goldengit.web.model.IssueSummaryResponse;
import com.goldengit.web.model.PullRequestSummaryResponse;
import com.goldengit.web.model.WeekOfCommitResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
@AllArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;
    private final WeekOfCommitResponseMapper weekOfCommitResponseMapper;
    private final PullRequestSummaryResponseMapper pullRequestSummaryResponseMapper;
    private final IssueSummaryResponseMapper issueSummaryResponseMapper;

    @GetMapping(value = "/{uuid}/metrics/commit-activity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeekOfCommitResponse>> getCommitActivityByWeek(@PathVariable("uuid") String uuid) {
        try {
            var metrics = metricsService.getCommitActivityByWeek(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(weekOfCommitResponseMapper.mapList(metrics));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{uuid}/metrics/accumulated-commits", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeekOfCommitResponse>> getAccumulatedCommitsByWeek(@PathVariable("uuid") String uuid) {
        try {
            var metrics = metricsService.getAccumulatedCommitsByWeek(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(weekOfCommitResponseMapper.mapList(metrics));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{uuid}/metrics/pull-requests-summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PullRequestSummaryResponse>> getPullRequestsByDate(@PathVariable("uuid") String uuid) {
        try {
            var summary = metricsService.getPullRequestsSummary(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(pullRequestSummaryResponseMapper.mapList(summary));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{uuid}/metrics/issues-summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IssueSummaryResponse>> getIssuesByDate(@PathVariable("uuid") String uuid) {
        try {
            var summary = metricsService.getIssuesSummary(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(issueSummaryResponseMapper.mapList(summary));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
