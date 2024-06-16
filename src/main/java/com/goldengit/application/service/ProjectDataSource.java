package com.goldengit.application.service;

import com.goldengit.application.dto.*;
import com.goldengit.application.query.FindIssueQuery;

import java.util.List;
import java.util.Optional;

public interface ProjectDataSource {
    List<ProjectDTO> findProjectsByQuery(String query);

    Optional<ProjectDTO> findRepoByFullName(String fullName);

    List<PullRequestDTO> findAllPullRequestByRepoName(String fullName);

    List<PullRequestDTO> findAllPullRequestByRepoName(String fullName, int maxResults);

    List<PullRequestDTO> findMergedPullRequestByRepoName(String fullName, int maxResults);

    public List<PullRequestReviewDTO> findPullRequestReviewsByRepoName(String fullName, int maxPullRequests);

    List<IssueDTO> findAllIssuesByRepoName(String fullName);

    List<IssueDTO> findIssues(FindIssueQuery query);

    List<WeekOfCommitDTO> getCommitActivity(String fullName);

    List<ContributorDTO> findAllContributorsByRepoName(String fullName, int maxResults);

    List<ReleaseDTO> findAllReleasesByRepoName(String fullName, int pageSize);
}
