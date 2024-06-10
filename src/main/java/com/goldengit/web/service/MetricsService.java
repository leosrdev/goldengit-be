package com.goldengit.web.service;

import com.goldengit.api.client.GitHubAPI;
import com.goldengit.api.schema.Issue;
import com.goldengit.api.schema.PullRequest;
import com.goldengit.api.schema.WeekOfCommit;
import com.goldengit.web.dto.IssueSummaryResponse;
import com.goldengit.web.dto.PullRequestSummaryResponse;
import com.goldengit.web.dto.WeekOfCommitResponse;
import com.goldengit.web.model.Project;
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
public class MetricsService extends BaseService {

    private final GitHubAPI gitApi;
    private final ProjectService projectService;

    @Cacheable(value = "git-repositories", key = "'commitActivityByWeek:' + #uuid")
    public List<WeekOfCommitResponse> getCommitActivityByWeek(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<WeekOfCommit> weekOfCommits = gitApi.getCommitActivity(project.getFullName());
        int weekNumber = 1;
        for (WeekOfCommit weekOfCommit : weekOfCommits) {
            weekOfCommit.week = weekNumber++;
        }

        return weekOfCommits.stream().parallel()
                .map(weekOfCommit -> WeekOfCommitResponse.builder()
                        .week(weekOfCommit.week)
                        .total(weekOfCommit.total)
                        .build()).collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'accumulatedCommitsByWeek:' + #uuid")
    public List<WeekOfCommitResponse> getAccumulatedCommitsByWeek(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<WeekOfCommit> weekOfCommits = gitApi.getCommitActivity(project.getFullName());
        int weekNumber = 1;
        int numberOfCommits = 0;
        for (WeekOfCommit week : weekOfCommits) {
            week.week = weekNumber++;
            week.total = (numberOfCommits += week.total);
        }

        return weekOfCommits.stream().parallel()
                .map(weekOfCommit -> WeekOfCommitResponse.builder()
                        .week(weekOfCommit.week)
                        .total(weekOfCommit.total)
                        .build()).collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'pullRequestsSummary:' + #uuid")
    public List<PullRequestSummaryResponse> getPullRequestsSummary(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
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

    @Cacheable(value = "git-repositories", key = "'issuesSummary:' + #uuid")
    public List<IssueSummaryResponse> getIssuesSummary(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<Issue> issues = gitApi.findAllIssuesByRepoName(project.getFullName());
        Map<String, Long> issuesGroupedByCreatedDate = issues
                .stream()
                .parallel()
                .collect(Collectors.groupingByConcurrent(
                        issue -> dateFormat(issue.created_at),
                        Collectors.counting()
                ));

        return issuesGroupedByCreatedDate.entrySet().stream()
                .map(entry -> IssueSummaryResponse.builder()
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
