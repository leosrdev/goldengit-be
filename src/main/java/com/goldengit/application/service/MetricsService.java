package com.goldengit.application.service;

import com.goldengit.application.dto.*;
import com.goldengit.domain.model.Project;
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

    private final ProjectDataSource projectDataSource;
    private final ProjectService projectService;

    @Cacheable(value = "git-repositories", key = "'commitActivityByWeek:' + #uuid")
    public List<WeekOfCommitDTO> getCommitActivityByWeek(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<WeekOfCommitDTO> weekOfCommitList = projectDataSource.getCommitActivity(project.getFullName());
        int weekNumber = 1;
        for (WeekOfCommitDTO weekOfCommitDTO : weekOfCommitList) {
            weekOfCommitDTO.setWeek(weekNumber++);
        }

        return weekOfCommitList;
    }

    @Cacheable(value = "git-repositories", key = "'accumulatedCommitsByWeek:' + #uuid")
    public List<WeekOfCommitDTO> getAccumulatedCommitsByWeek(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<WeekOfCommitDTO> weekOfCommitList = projectDataSource.getCommitActivity(project.getFullName());
        int weekNumber = 1;
        long numberOfCommits = 0;
        for (WeekOfCommitDTO week : weekOfCommitList) {
            week.setWeek(weekNumber++);
            week.setTotal(numberOfCommits += week.getTotal());
        }

        return weekOfCommitList;
    }

    @Cacheable(value = "git-repositories", key = "'pullRequestsSummary:' + #uuid")
    public List<PullRequestSummaryDTO> getPullRequestsSummary(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<PullRequestDTO> pullRequestList = projectDataSource.findAllPullRequestByRepoName(project.getFullName());
        Map<String, Long> pullRequestsGroupedByCreatedDate = pullRequestList
                .stream()
                .parallel()
                .collect(Collectors.groupingByConcurrent(
                        pullRequestDTO -> dateFormat(pullRequestDTO.getCreatedAt()),
                        Collectors.counting()
                ));

        return pullRequestsGroupedByCreatedDate.entrySet().stream()
                .map(entry -> PullRequestSummaryDTO.builder()
                        .date(entry.getKey())
                        .total(entry.getValue())
                        .build())
                .sorted(Comparator.comparing(p -> LocalDate.parse(p.getDate())))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'issuesSummary:' + #uuid")
    public List<IssueSummaryDTO> getIssuesSummary(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<IssueDTO> issueSchemas = projectDataSource.findAllIssuesByRepoName(project.getFullName());
        Map<String, Long> issuesGroupedByCreatedDate = issueSchemas
                .stream()
                .parallel()
                .collect(Collectors.groupingByConcurrent(
                        issueDTO -> dateFormat(issueDTO.getCreatedAt()),
                        Collectors.counting()
                ));

        return issuesGroupedByCreatedDate.entrySet().stream()
                .map(entry -> IssueSummaryDTO.builder()
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
