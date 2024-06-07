package com.goldengit.web.controller;

import com.goldengit.restclient.service.OverviewService;
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
public class OverviewController {
    private final OverviewService overviewService;
    @GetMapping(value = "/{uuid}/overview/releases", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReleaseResponse>> getRepositoryReleases(@PathVariable("uuid") String uuid) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(overviewService.findAllReleasesByRepo(uuid));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
