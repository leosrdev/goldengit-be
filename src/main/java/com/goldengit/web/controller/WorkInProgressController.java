package com.goldengit.web.controller;

import com.goldengit.application.service.ProjectService;
import com.goldengit.web.mapper.IssueResponseMapper;
import com.goldengit.web.mapper.PullRequestResponseMapper;
import com.goldengit.web.model.IssueResponse;
import com.goldengit.web.model.PullRequestResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
@AllArgsConstructor
public class WorkInProgressController {
    private final ProjectService projectService;
    private final PullRequestResponseMapper pullRequestResponseMapper;
    private final IssueResponseMapper issueResponseMapper;

    @GetMapping("/{uuid}/pulls")
    public ResponseEntity<List<PullRequestResponse>> findLatestPullRequestByRepoUuid(@PathVariable("uuid") String uuid) {
        try {
            var pullRequests = projectService.findLatestPullRequestByRepoUuid(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(pullRequestResponseMapper.mapList(pullRequests));
        } catch (BadRequestException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{uuid}/issues")
    public ResponseEntity<List<IssueResponse>> findLatestIssues(@PathVariable("uuid") String uuid) {
        try {
            var issues = projectService.findLatestIssues(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(issueResponseMapper.mapList(issues));
        } catch (BadRequestException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
