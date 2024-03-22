package com.goldengit.web.controller;

import com.goldengit.restclient.service.GitService;
import com.goldengit.web.dto.RepoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
}
