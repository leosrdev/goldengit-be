package com.goldengit.restclient.api;

import com.goldengit.restclient.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GitHubAPI extends BaseAPI {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Repositories findRepoByQuery(String query) {
        return webClientBuilder.build()
                .get()
                .uri(String.format("https://api.github.com/search/repositories?q=%s", query))
                .accept(APPLICATION_JSON_GITHUB)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                .retrieve()
                .bodyToMono(Repositories.class)
                .block();
    }

    public Repository findRepoByFullName(String fullName) {
        return webClientBuilder.build()
                .get()
                .uri(String.format("https://api.github.com/repos/%s", fullName))
                .accept(APPLICATION_JSON_GITHUB)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                .retrieve()
                .bodyToMono(Repository.class)
                .block();
    }

    public List<PullRequest> findPullRequestByRepoName(String owner, String repo) {
        PullRequest[] pullRequests = null;
        try {
            pullRequests = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/%s/pulls", owner, repo))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(PullRequest[].class)
                    .block();
        } catch (WebClientResponseException exception) {
            return new ArrayList<>();
        }

        return Arrays.asList(pullRequests != null ? pullRequests : new PullRequest[0]);
    }

    public List<PullRequest> findAllPullRequestByRepoName(String fullName) {
        PullRequest[] pullRequests = null;
        try {
            pullRequests = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/pulls?state=all&per_page=100&direction=desc", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(PullRequest[].class)
                    .block();
        } catch (WebClientResponseException exception) {
            return new ArrayList<>();
        }

        return pullRequests != null ? Arrays.asList(pullRequests) : List.of();
    }

    public List<Issue> findAllIssuesByRepoName(String fullName) {
        Issue[] issues = null;
        try {
            issues = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/issues?state=all&per_page=100&direction=desc", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(Issue[].class)
                    .block();
        } catch (WebClientResponseException exception) {
            return List.of();
        }
        return issues != null ? Arrays.asList(issues) : List.of();
    }

    public List<WeekOfCommit> getCommitActivity(String fullName) {
        WeekOfCommit[] commits = webClientBuilder.build()
                .get()
                .uri(String.format("https://api.github.com/repos/%s/stats/commit_activity", fullName))
                .accept(APPLICATION_JSON_GITHUB)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                .retrieve()
                .bodyToMono(WeekOfCommit[].class)
                .block();
        return commits != null ? Arrays.asList(commits) : List.of();
    }
}
