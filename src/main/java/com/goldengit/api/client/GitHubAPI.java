package com.goldengit.api.client;

import com.goldengit.api.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class GitHubAPI {

    @Value("${github.api.token}")
    protected String apiToken;
    @Autowired
    private WebClient.Builder webClientBuilder;
    protected final static MediaType APPLICATION_JSON_GITHUB = MediaType.valueOf("application/vnd.github+json");

    public Repositories findRepoByQuery(String query) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/search/repositories?q=%s", query))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(Repositories.class)
                    .block();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception.getCause());
            return new Repositories();
        }
    }

    public Repository findRepoByFullName(String fullName) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(Repository.class)
                    .block();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return new Repository();
        }
    }

    public List<PullRequest> findAllPullRequestByRepoName(String fullName) {
        return findAllPullRequestByRepoName(fullName, 100, "desc");
    }

    public List<PullRequest> findAllPullRequestByRepoName(String fullName, int pageSize, String direction) {
        try {
            PullRequest[] pullRequests = webClientBuilder.build()
                    .get()
                    .uri("https://api.github.com/repos/%s/pulls?state=all&per_page=%s&direction=%s".formatted(fullName, pageSize, direction))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(PullRequest[].class)
                    .block();
            return pullRequests != null ? Arrays.asList(pullRequests) : List.of();
        } catch (WebClientResponseException exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<Issue> findAllIssuesByRepoName(String fullName) {
        try {
            Issue[] issues = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/issues?state=all&per_page=100&direction=desc", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(Issue[].class)
                    .block();
            return issues != null ? Arrays.asList(issues) : List.of();
        } catch (WebClientResponseException exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<WeekOfCommit> getCommitActivity(String fullName) {
        try {
            WeekOfCommit[] commits = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/stats/commit_activity", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(WeekOfCommit[].class)
                    .block();
            return commits != null ? Arrays.asList(commits) : List.of();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<Contributor> findAllContributorsByRepoName(String fullName, int pageSize) {
        Contributor[] contributors = null;
        try {
            contributors = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/contributors?per_page=%s", fullName, pageSize))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(Contributor[].class)
                    .block();
        } catch (WebClientResponseException exception) {
            return List.of();
        }
        return contributors != null ? Arrays.asList(contributors) : List.of();
    }

    public List<Release> findAllReleasesByRepoName(String fullName, int pageSize) {
        Release[] releases = null;
        try {
            releases = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/releases?per_page=%s", fullName, pageSize))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(Release[].class)
                    .block();
        } catch (WebClientResponseException exception) {
            return List.of();
        }
        return releases != null ? Arrays.asList(releases) : List.of();
    }
}
