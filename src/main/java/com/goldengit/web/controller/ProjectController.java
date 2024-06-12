package com.goldengit.web.controller;

import com.goldengit.application.service.ProjectService;
import com.goldengit.web.mapper.ProjectResponseMapper;
import com.goldengit.web.mapper.PullRequestResponseMapper;
import com.goldengit.web.model.ProjectResponse;
import com.goldengit.web.model.PullRequestResponse;
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
    private final ProjectResponseMapper projectResponseMapper;
    private final PullRequestResponseMapper pullRequestResponseMapper;

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponse>> getRepoByQuery(@RequestParam("q") String query) {
        var projects = projectService.findRepoByQuery(query);
        var responses = projectResponseMapper.mapList(projects);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{uuid}/pulls")
    public ResponseEntity<List<PullRequestResponse>> getPullRequests(@PathVariable("uuid") String uuid) {
        try {
            var pullRequests = projectService.findPullRequestByRepoUuid(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(pullRequestResponseMapper.mapList(pullRequests));
        } catch (BadRequestException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/popular", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProjectResponse>> listPopularRepositories() {
        var projects = projectService.listPopularProjects();
        return ResponseEntity.status(HttpStatus.OK)
                .body(projectResponseMapper.mapList(projects));
    }
}
