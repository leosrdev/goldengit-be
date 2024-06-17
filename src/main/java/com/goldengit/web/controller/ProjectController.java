package com.goldengit.web.controller;

import com.goldengit.application.service.ProjectService;
import com.goldengit.web.mapper.ProjectResponseMapper;
import com.goldengit.web.model.ProjectResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectResponseMapper projectResponseMapper;


    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponse>> getRepoByQuery(@RequestParam("q") String query) {
        var projects = projectService.findRepoByQuery(query);
        var responses = projectResponseMapper.mapList(projects);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping(value = "/popular", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProjectResponse>> listPopularRepositories() {
        var projects = projectService.listPopularProjects();
        return ResponseEntity.status(HttpStatus.OK)
                .body(projectResponseMapper.mapList(projects));
    }
}
