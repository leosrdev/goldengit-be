package com.goldengit.restclient.service;

import com.goldengit.restclient.api.GitHubAPI;
import com.goldengit.restclient.schema.PullRequest;
import com.goldengit.restclient.schema.PullRequestSummary;
import com.goldengit.restclient.schema.WeekOfCommit;
import com.goldengit.web.dto.WeekOfCommitResponse;
import com.goldengit.web.model.GitProject;
import com.goldengit.web.service.GitProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final GitHubAPI gitApi;
    private final GitProjectService gitProjectService;

    @Cacheable(value = "git-repositories", key = "'commitActivityByWeek:' + #uuid")
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
                        .build()).collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'accumulatedCommitsByWeek:' + #uuid")
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
                        .build()).collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'groupedPullRequestByRepo:' + #uuid")
    public List<PullRequestSummary> getGroupedPullRequestByRepo(String uuid) throws BadRequestException {
        Optional<GitProject> optionalProject = gitProjectService.findById(uuid);
        return optionalProject.map(gitProject -> {
                    List<PullRequest> pullRequests =
                            gitApi.findAllPullRequestByRepoName(optionalProject.get().getFullName());

                    Map<String, Long> pullRequestsGroupedByCreatedDate = pullRequests
                            .stream()
                            .parallel()
                            .collect(Collectors.groupingByConcurrent(
                                    pullRequest -> dateFormat(pullRequest.created_at),
                                    Collectors.counting()
                            ));

                    return pullRequestsGroupedByCreatedDate.entrySet().stream()
                            .map(entry -> PullRequestSummary.builder()
                                    .date(entry.getKey())
                                    .total(entry.getValue())
                                    .build())
                            .sorted(Comparator.comparing(p -> LocalDate.parse(p.getDate())))
                            .collect(Collectors.toList());

                })
                .orElseThrow(() -> {
                    log.error("No project found with uuid: " + uuid);
                    return new BadRequestException("No project found with uuid: " + uuid);
                });
    }

    private String dateFormat(String input) {
        ZonedDateTime zdt = ZonedDateTime.parse(input);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return zdt.format(formatter);
    }
}
