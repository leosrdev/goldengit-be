package com.goldengit.application.service;

import com.goldengit.domain.model.Project;
import com.goldengit.infra.api.github.client.GitHubClient;
import com.goldengit.infra.api.github.schema.IssueSchema;
import com.goldengit.infra.api.github.schema.PullRequestSchema;
import com.goldengit.infra.api.github.schema.WeekOfCommitSchema;
import com.goldengit.web.model.IssueSummaryResponse;
import com.goldengit.web.model.PullRequestSummaryResponse;
import com.goldengit.web.model.WeekOfCommitResponse;
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

    private final GitHubClient gitApi;
    private final ProjectService projectService;

    @Cacheable(value = "git-repositories", key = "'commitActivityByWeek:' + #uuid")
    public List<WeekOfCommitResponse> getCommitActivityByWeek(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<WeekOfCommitSchema> weekOfCommitSchemas = gitApi.getCommitActivity(project.getFullName());
        int weekNumber = 1;
        for (WeekOfCommitSchema weekOfCommitSchema : weekOfCommitSchemas) {
            weekOfCommitSchema.week = weekNumber++;
        }

        return weekOfCommitSchemas.stream().parallel()
                .map(weekOfCommitSchema -> WeekOfCommitResponse.builder()
                        .week(weekOfCommitSchema.week)
                        .total(weekOfCommitSchema.total)
                        .build()).collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'accumulatedCommitsByWeek:' + #uuid")
    public List<WeekOfCommitResponse> getAccumulatedCommitsByWeek(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<WeekOfCommitSchema> weekOfCommitSchemas = gitApi.getCommitActivity(project.getFullName());
        int weekNumber = 1;
        int numberOfCommits = 0;
        for (WeekOfCommitSchema week : weekOfCommitSchemas) {
            week.week = weekNumber++;
            week.total = (numberOfCommits += week.total);
        }

        return weekOfCommitSchemas.stream().parallel()
                .map(weekOfCommitSchema -> WeekOfCommitResponse.builder()
                        .week(weekOfCommitSchema.week)
                        .total(weekOfCommitSchema.total)
                        .build()).collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'pullRequestsSummary:' + #uuid")
    public List<PullRequestSummaryResponse> getPullRequestsSummary(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<PullRequestSchema> pullRequestSchemas = gitApi.findAllPullRequestByRepoName(project.getFullName());
        Map<String, Long> pullRequestsGroupedByCreatedDate = pullRequestSchemas
                .stream()
                .parallel()
                .collect(Collectors.groupingByConcurrent(
                        pullRequestSchema -> dateFormat(pullRequestSchema.created_at),
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
        List<IssueSchema> issueSchemas = gitApi.findAllIssuesByRepoName(project.getFullName());
        Map<String, Long> issuesGroupedByCreatedDate = issueSchemas
                .stream()
                .parallel()
                .collect(Collectors.groupingByConcurrent(
                        issueSchema -> dateFormat(issueSchema.created_at),
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
