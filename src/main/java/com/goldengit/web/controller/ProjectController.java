package com.goldengit.web.controller;

import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.RepoResponse;
import com.goldengit.web.service.ProjectService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/search")
    public ResponseEntity<List<RepoResponse>> getRepoByQuery(@RequestParam("q") String query) {
        return ResponseEntity.ok(projectService.findRepoByQuery(query));
    }

    @GetMapping("/{uuid}/pulls")
    public ResponseEntity<List<PullRequestResponse>> getPullRequests(@PathVariable("uuid") String uuid) {
        try {
            List<PullRequestResponse> pullRequests = projectService.findPullRequestByRepoUuid(uuid);
            return ResponseEntity.status(HttpStatus.OK).body(pullRequests);
        } catch (BadRequestException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/popular", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RepoResponse>> listPopularRepositories() {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.listPopularRepositories());
    }
}
