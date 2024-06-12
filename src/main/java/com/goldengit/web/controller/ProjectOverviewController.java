package com.goldengit.web.controller;

import com.goldengit.application.service.ProjectOverviewService;
import com.goldengit.web.mapper.ProjectSummaryResponseMapper;
import com.goldengit.web.mapper.ReleaseResponseMapper;
import com.goldengit.web.model.ProjectSummaryResponse;
import com.goldengit.web.model.ReleaseResponse;
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
    private final ProjectSummaryResponseMapper projectSummaryResponseMapper;
    private final ReleaseResponseMapper releaseResponseMapper;

    @GetMapping(value = "/{uuid}/metrics/project-summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectSummaryResponse> getProjectSummary(@PathVariable("uuid") String uuid) {
        try {
            var summary = projectOverviewService.generateProjectSummary(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(projectSummaryResponseMapper.map(summary));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{uuid}/overview/releases", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReleaseResponse>> getRepositoryReleases(@PathVariable("uuid") String uuid) {
        try {
            var releases = projectOverviewService.findAllReleasesByRepo(uuid);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(releaseResponseMapper.mapList(releases));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
