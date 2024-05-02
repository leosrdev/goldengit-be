package com.goldengit.web.controller;

import com.goldengit.restclient.service.GitService;
import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.RepoResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
@AllArgsConstructor
public class RepoController {

    private final GitService gitService;

    @GetMapping("/search")
    public ResponseEntity<List<RepoResponse>> getRepoByQuery(@RequestParam("q") String query) {
        return ResponseEntity.ok(gitService.findRepoByQuery(query));
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
