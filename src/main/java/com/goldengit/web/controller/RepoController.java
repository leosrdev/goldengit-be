package com.goldengit.web.controller;

import com.goldengit.restclient.service.GitService;
import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.RepoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
@RequiredArgsConstructor
public class RepoController {

    @Autowired
    private final GitService gitService;

    @GetMapping("/")
    public List<RepoResponse> getRepoByQuery(@RequestParam("q") String query) {
        return gitService.findRepoByQuery(query);
    }

    @GetMapping("/{owner}/{repo}/pulls")
    public ResponseEntity<List<PullRequestResponse>> getPullRequests(@PathVariable("owner") String owner, @PathVariable("repo") String repo) {
        List<PullRequestResponse> pullRequests = gitService.findPullRequestByRepoName(owner, repo);
        if (pullRequests != null && !pullRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(pullRequests);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
