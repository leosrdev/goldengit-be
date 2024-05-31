package com.goldengit.restclient.service;

import com.goldengit.restclient.api.GitHubAPI;
import com.goldengit.restclient.schema.WeekOfCommit;
import com.goldengit.web.dto.WeekOfCommitResponse;
import com.goldengit.web.model.GitProject;
import com.goldengit.web.service.GitProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final GitHubAPI gitApi;
    private final GitProjectService gitProjectService;

    public List<WeekOfCommitResponse> getCommitActivityByWeek(String uuid) throws BadRequestException {
        Optional<GitProject> optionalProject = gitProjectService.findById(uuid);
        if (optionalProject.isEmpty()) {
            throw new BadRequestException();
        }

        List<WeekOfCommit> weekOfCommits = gitApi.getCommitActivity(optionalProject.get().getFullName());
        int weekNumber = 1;
        for (WeekOfCommit week : weekOfCommits) {
            week.setWeek(weekNumber++);
        }

        return weekOfCommits.stream().parallel()
                .map(weekOfCommit -> WeekOfCommitResponse.builder()
                        .week(weekOfCommit.getWeek())
                        .total(weekOfCommit.getTotal())
                        .build()).toList();
    }

    public List<WeekOfCommitResponse> getAccumulatedCommitsByWeek(String uuid) throws BadRequestException {
        Optional<GitProject> optionalProject = gitProjectService.findById(uuid);
        if (optionalProject.isEmpty()) {
            throw new BadRequestException();
        }

        List<WeekOfCommit> weekOfCommits = gitApi.getCommitActivity(optionalProject.get().getFullName());
        int weekNumber = 1;
        int numberOfCommits = 0;
        for (WeekOfCommit week : weekOfCommits) {
            week.setWeek(weekNumber++);
            week.setTotal(numberOfCommits += week.getTotal());
        }

        return weekOfCommits.stream().parallel()
                .map(weekOfCommit -> WeekOfCommitResponse.builder()
                        .week(weekOfCommit.getWeek())
                        .total(weekOfCommit.getTotal())
                        .build()).toList();
    }
}
