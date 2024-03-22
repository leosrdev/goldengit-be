package com.goldengit.web.controller;

import com.goldengit.restclient.service.GitService;
import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.RepoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RepoController {

    @Autowired
    private final GitService gitService;

    @GetMapping("/repos")
    public List<RepoResponse> getRepoByQuery(@RequestParam("q") String query) {
        return gitService.findRepoByQuery(query);
    }

    @GetMapping("/repos/pulls")
    public ResponseEntity<List<PullRequestResponse>> getPullRequests(@RequestParam("repoFullName") String repoFullName) {
        List<PullRequestResponse> pullRequests = gitService.findPullRequestByRepoName(repoFullName);
        if (pullRequests != null && !pullRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(pullRequests);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
