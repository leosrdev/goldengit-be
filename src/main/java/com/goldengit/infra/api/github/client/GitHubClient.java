package com.goldengit.infra.api.github.client;

import com.goldengit.application.dto.*;
import com.goldengit.application.query.FindIssueQuery;
import com.goldengit.application.service.ProjectDataSource;
import com.goldengit.infra.api.github.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class GitHubClient implements ProjectDataSource {
    private final GitHub gitHubApi;
    private final ProjectSchemaMapper projectSchemaMapper;
    private final PullRequestSchemaMapper pullRequestSchemaMapper;
    private final PullRequestReviewSchemaMapper pullRequestReviewSchemaMapper;
    private final IssueSchemaMapper issueSchemaMapper;
    private final WeekOfCommitSchemaMapper weekOfCommitSchemaMapper;
    private final ContributorSchemaMapper contributorSchemaMapper;
    private final ReleaseSchemaMapper releaseSchemaMapper;

    public List<ProjectDTO> findProjectsByQuery(String query) {
        try {
            // TODO: warning: for each result a request will be made to get repo data from API
            var pagedSearchIterable = gitHubApi.searchRepositories().q(query).list();
            var repositories = fetchRecords(pagedSearchIterable, 50);
            return projectSchemaMapper.mapList(repositories);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception.getCause());
            return Collections.emptyList();
        }
    }

    public Optional<ProjectDTO> findRepoByFullName(String fullName) {
        try {
            var repository = gitHubApi.getRepository(fullName);
            if (repository == null) {
                return Optional.empty();
            }
            return Optional.of(projectSchemaMapper.map(repository));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Optional.empty();
        }
    }

    public List<PullRequestDTO> findAllPullRequestByRepoName(String fullName) {
        return findAllPullRequestByRepoName(fullName, 100);
    }

    public List<PullRequestDTO> findAllPullRequestByRepoName(String fullName, int maxResults) {
        try {
            PagedIterable<GHPullRequest> pagedIterable = gitHubApi.getRepository(fullName)
                    .queryPullRequests().list();
            var pulls = fetchRecords(pagedIterable, maxResults);
            return pullRequestSchemaMapper.mapList(pulls);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    public List<PullRequestDTO> findMergedPullRequestByRepoName(String fullName, int maxResults) {
        try {
            PagedIterable<GHPullRequest> pagedIterable = gitHubApi.getRepository(fullName)
                    .searchPullRequests().isMerged().list();
            var pulls = fetchRecords(pagedIterable, maxResults);
            return pullRequestSchemaMapper.mapList(pulls);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    public List<PullRequestReviewDTO> findPullRequestReviewsByRepoName(String fullName, int maxPullRequests) {
        try {
            PagedIterable<GHPullRequest> pagedIterable = gitHubApi.getRepository(fullName)
                    .searchPullRequests().isMerged().list();
            var pullRequests = fetchRecords(pagedIterable, maxPullRequests);

            List<PullRequestReviewDTO> reviews = new ArrayList<>();
            pullRequests.stream().parallel().forEach(pullRequest -> {
                var pagedIterableReview = pullRequest.listReviews();
                var reviewsList = fetchRecords(pagedIterableReview, 10);
                var reviewsDTO = pullRequestReviewSchemaMapper.mapList(reviewsList);
                for (PullRequestReviewDTO dto : reviewsDTO) {
                    dto.setPullRequestId(pullRequest.getId());
                    dto.setPullRequestNumber(pullRequest.getNumber());
                }
                reviews.addAll(reviewsDTO);
            });

            return reviews;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    public List<IssueDTO> findAllIssuesByRepoName(String fullName) {
        try {
            var pagedIterable = gitHubApi.getRepository(fullName).queryIssues().list();
            var issues = fetchRecords(pagedIterable, 100);
            return issueSchemaMapper.mapList(issues);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<IssueDTO> findIssues(FindIssueQuery query) {
        try {
            var repository = gitHubApi.getRepository(query.getRepositoryName());
            var queryBuilder = repository.queryIssues();
            if (query.getState() != null) {
                queryBuilder.state(query.getState());
            }
            var pagedIterable = queryBuilder.list();
            var issues = fetchRecords(pagedIterable, query.getMaxResults() > 0 ? query.getMaxResults() : 100);
            return issueSchemaMapper.mapList(issues);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    public List<WeekOfCommitDTO> getCommitActivity(String fullName) {
        try {
            var pagedIterable = gitHubApi.getRepository(fullName).getStatistics().getCommitActivity();
            var commitActivity = fetchRecords(pagedIterable, 100);
            return weekOfCommitSchemaMapper.mapList(commitActivity);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    public List<ContributorDTO> findAllContributorsByRepoName(String fullName, int maxResults) {
        try {
            var pagedIterable = gitHubApi.getRepository(fullName).listContributors();
            var collaborators = fetchRecords(pagedIterable, maxResults);
            return contributorSchemaMapper.mapList(collaborators);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<ReleaseDTO> findAllReleasesByRepoName(String fullName, int maxResults) {
        try {
            var pagedIterable = gitHubApi.getRepository(fullName).listReleases();
            var releases = fetchRecords(pagedIterable, maxResults);
            return releaseSchemaMapper.mapList(releases);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    private <T> List<T> fetchRecords(PagedIterable<T> pagedIterable, int maxResults) {
        PagedIterator<T> iterator = pagedIterable._iterator(Math.min(100, maxResults));
        List<T> record = new ArrayList<>();
        for (int i = 1; i <= maxResults && iterator.hasNext(); i++) {
            record.add(iterator.next());
        }
        return record;
    }
}
