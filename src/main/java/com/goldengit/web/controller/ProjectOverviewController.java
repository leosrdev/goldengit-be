package com.goldengit.web.controller;

import com.goldengit.application.service.ProjectOverviewService;
import com.goldengit.web.dto.ProjectSummaryResponse;
import com.goldengit.web.dto.ReleaseResponse;
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
public class ProjectOverviewController {
    private final ProjectOverviewService projectOverviewService;

    @GetMapping(value = "/{uuid}/metrics/project-summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectSummaryResponse> getProjectSummary(@PathVariable("uuid") String uuid) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(projectOverviewService.generateProjectSummary(uuid));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{uuid}/overview/releases", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReleaseResponse>> getRepositoryReleases(@PathVariable("uuid") String uuid) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(projectOverviewService.findAllReleasesByRepo(uuid));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
