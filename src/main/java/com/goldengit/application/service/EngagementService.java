package com.goldengit.application.service;

import com.goldengit.application.dto.*;
import com.goldengit.application.query.FindIssueQuery;
import com.goldengit.domain.model.Project;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.kohsuke.github.GHIssueState;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EngagementService extends BaseService {
    private final ProjectDataSource projectDataSource;
    private final ProjectService projectService;

    @Cacheable(value = "git-repositories", key = "'contributors:' + #uuid")
    public List<ContributorDTO> findTopContributorsByRepo(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        return projectDataSource.findAllContributorsByRepoName(project.getFullName(), 10);
    }

    @Cacheable(value = "git-repositories", key = "'pull-cycle:' + #uuid")
    public List<PullRequestDTO> findMergedPullRequests(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        var pullRequests = projectDataSource.findMergedPullRequestByRepoName(project.getFullName(), 100);
        return pullRequests.stream().parallel()
                .sorted(Comparator.comparingInt(PullRequestDTO::getNumber))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'pull-reviews:' + #uuid")
    public List<PullRequestReviewDTO> findPullRequestReviewsForRepo(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        var reviews = projectDataSource.findPullRequestReviewsByRepoName(project.getFullName(), 50);
        return reviews.stream().parallel()
                .sorted(Comparator.comparingInt(PullRequestReviewDTO::getPullRequestNumber))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'reviews-summary:' + #uuid")
    public List<ReviewerDTO> getPullRequestReviewsSummaryForRepo(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        var reviews = projectDataSource.findPullRequestReviewsByRepoName(project.getFullName(), 50);

        Map<String, PullRequestReviewDTO> reviewsMap = reviews.stream()
                .collect(Collectors.toMap(
                        PullRequestReviewDTO::getUserLogin,
                        review -> review,
                        (existing, replacement) -> existing,
                        HashMap::new
                ));

        var reviewGroupedByLogin = reviews.stream().parallel()
                .collect(Collectors.groupingByConcurrent(
                        PullRequestReviewDTO::getUserLogin,
                        Collectors.counting()
                ));

        return reviewGroupedByLogin.entrySet().stream()
                .map(entry -> {
                    var pullRequestReviewDTO = reviewsMap.get(entry.getKey());
                    return ReviewerDTO.builder()
                            .login(entry.getKey())
                            .reviews(entry.getValue())
                            .name(pullRequestReviewDTO.getUserName())
                            .avatarUrl(pullRequestReviewDTO.getUserAvatarUrl())
                            .htmlUrl(pullRequestReviewDTO.getUserHtmlUrl())
                            .build();
                })
                .sorted(Comparator.comparing(ReviewerDTO::getReviews).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'issue-cycle:' + #uuid")
    public List<IssueDTO> findSolvedIssues(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        var issues = projectDataSource.findIssues(
                new FindIssueQuery(project.getFullName(), GHIssueState.CLOSED, 100)
        );
        return issues.stream().parallel()
                .sorted(Comparator.comparingInt(IssueDTO::getNumber))
                .collect(Collectors.toList());
    }
}
