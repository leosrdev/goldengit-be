package com.goldengit.web.controller;

import com.goldengit.application.service.EngagementService;
import com.goldengit.web.mapper.ContributorResponseMapper;
import com.goldengit.web.mapper.IssueResponseMapper;
import com.goldengit.web.mapper.PullRequestResponseMapper;
import com.goldengit.web.model.ContributorResponse;
import com.goldengit.web.model.IssueResponse;
import com.goldengit.web.model.PullRequestResponse;
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
public class EngagementController {
    private final EngagementService engagementService;
    private final ContributorResponseMapper contributorResponseMapper;
    private final PullRequestResponseMapper pullRequestResponseMapper;
    private final IssueResponseMapper issueResponseMapper;

    @GetMapping(value = "/{uuid}/engagement/contributors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContributorResponse>> getRepositoryContributors(@PathVariable("uuid") String uuid) {
        try {
            var contributors = engagementService.findTopContributorsByRepo(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(contributorResponseMapper.mapList(contributors));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{uuid}/engagement/pull-request-cycle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PullRequestResponse>> generatePullRequestsCycleTime(@PathVariable("uuid") String uuid) {
        try {
            var pullRequests = engagementService.findMergedPullRequests(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(pullRequestResponseMapper.mapList(pullRequests));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{uuid}/engagement/issue-cycle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IssueResponse>> generateIssuesCycleTime(@PathVariable("uuid") String uuid) {
        try {
            var issues = engagementService.findSolvedIssues(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(issueResponseMapper.mapList(issues));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
