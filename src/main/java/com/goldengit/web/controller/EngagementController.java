package com.goldengit.web.controller;

import com.goldengit.application.service.EngagementService;
import com.goldengit.web.mapper.ContributorResponseMapper;
import com.goldengit.web.model.ContributorResponse;
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
}
