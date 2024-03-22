package com.goldengit.restclient.service;

import com.goldengit.restclient.repository.GitRepository;
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

    private final GitRepository gitRepository;

    public List<RepoResponse> findRepoByQuery(String query) {
        Repositories repositories = gitRepository.findRepoByQuery(query);

        return repositories.getItems().stream().map(repository ->
                RepoResponse.builder()
                        .id(repository.id)
                        .name(repository.name)
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

    public List<PullRequestResponse> findPullRequestByRepoName(String fullName) {
        List<PullRequest> pullRequests = gitRepository.findPullRequestByRepoName(fullName);

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
