package com.goldengit.restclient.service;

import com.goldengit.restclient.api.GitHubAPI;
import com.goldengit.restclient.schema.PullRequest;
import com.goldengit.restclient.schema.Repositories;
import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.RepoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitService {

    private final GitHubAPI gitApi;

    public List<RepoResponse> findRepoByQuery(String query) {
        Repositories repositories = gitApi.findRepoByQuery(query);

        return repositories.getItems().stream().map(repository ->
                RepoResponse.builder()
                        .fullName(repository.full_name)
                        .description(repository.description)
                        .stars(repository.stargazers_count)
                        .forks(repository.forks_count)
                        .watchers(repository.watchers_count)
                        .defaultBranch(repository.default_branch)
                        .openIssues(repository.open_issues_count)
                        .build()
        ).collect(Collectors.toList());
    }

    public List<PullRequestResponse> findPullRequestByRepoName(String owner, String repo) {
        List<PullRequest> pullRequests = gitApi.findPullRequestByRepoName(owner, repo);

        return pullRequests.stream().map(pullRequest ->
                PullRequestResponse.builder()
                        .id(pullRequest.id)
                        .number(pullRequest.number)
                        .title(pullRequest.title)
                        .state(pullRequest.state)
                        .createdAt(pullRequest.created_at)
                        .closedAt(pullRequest.closed_at)
                        .body(pullRequest.body)
                        .build()
        ).collect(Collectors.toList());
    }
}
