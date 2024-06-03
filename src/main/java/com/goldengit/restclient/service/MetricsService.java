package com.goldengit.restclient.service;

import com.goldengit.restclient.api.GitHubAPI;
import com.goldengit.restclient.schema.PullRequest;
import com.goldengit.restclient.schema.PullRequestSummaryResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final GitHubAPI gitApi;
    private final GitProjectService gitProjectService;

    @Cacheable(value = "git-repositories", key = "'projects:' + #uuid")
    private GitProject getProjectByUUID(String uuid) throws BadRequestException {
        return gitProjectService.findById(uuid)
                .orElseThrow(() -> {
                    log.error("No project found with uuid: " + uuid);
                    return new BadRequestException("No project found with uuid: " + uuid);
                });
    }

    @Cacheable(value = "git-repositories", key = "'commitActivityByWeek:' + #uuid")
    public List<WeekOfCommitResponse> getCommitActivityByWeek(String uuid) throws BadRequestException {
        GitProject project = getProjectByUUID(uuid);
        List<WeekOfCommit> weekOfCommits = gitApi.getCommitActivity(project.getFullName());
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
        GitProject project = getProjectByUUID(uuid);
        List<WeekOfCommit> weekOfCommits = gitApi.getCommitActivity(project.getFullName());
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
    public List<PullRequestSummaryResponse> getPullRequestsSummary(String uuid) throws BadRequestException {
        GitProject project = getProjectByUUID(uuid);
        List<PullRequest> pullRequests = gitApi.findAllPullRequestByRepoName(project.getFullName());
        Map<String, Long> pullRequestsGroupedByCreatedDate = pullRequests
                .stream()
                .parallel()
                .collect(Collectors.groupingByConcurrent(
                        pullRequest -> dateFormat(pullRequest.created_at),
                        Collectors.counting()
                ));

        return pullRequestsGroupedByCreatedDate.entrySet().stream()
                .map(entry -> PullRequestSummaryResponse.builder()
                        .date(entry.getKey())
                        .total(entry.getValue())
                        .build())
                .sorted(Comparator.comparing(p -> LocalDate.parse(p.getDate())))
                .collect(Collectors.toList());
    }

    private String dateFormat(String input) {
        ZonedDateTime zdt = ZonedDateTime.parse(input);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return zdt.format(formatter);
    }
}
