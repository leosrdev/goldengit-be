package com.goldengit.web.controller;

import com.goldengit.restclient.schema.PullRequestSummary;
import com.goldengit.restclient.service.MetricsService;
import com.goldengit.web.dto.WeekOfCommitResponse;
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

    @GetMapping(value = "/{uuid}/metrics/commit-activity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeekOfCommitResponse>> getCommitActivityByWeek(@PathVariable("uuid") String uuid) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(metricsService.getCommitActivityByWeek(uuid));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{uuid}/metrics/accumulated-commits", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeekOfCommitResponse>> getAccumulatedCommitsByWeek(@PathVariable("uuid") String uuid) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(metricsService.getAccumulatedCommitsByWeek(uuid));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{uuid}/metrics/pull-requests-summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PullRequestSummary>> getPullRequestsByDate(@PathVariable("uuid") String uuid) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(metricsService.getGroupedPullRequestByRepo(uuid));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
