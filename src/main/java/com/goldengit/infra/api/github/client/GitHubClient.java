package com.goldengit.infra.api.github.client;

import com.goldengit.infra.api.github.schema.*;
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
public class GitHubClient {

    @Value("${github.api.token}")
    protected String apiToken;
    @Autowired
    private WebClient.Builder webClientBuilder;
    protected final static MediaType APPLICATION_JSON_GITHUB = MediaType.valueOf("application/vnd.github+json");

    public RepositoriesSchema findRepoByQuery(String query) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/search/repositories?q=%s", query))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(RepositoriesSchema.class)
                    .block();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception.getCause());
            return new RepositoriesSchema();
        }
    }

    public RepositorySchema findRepoByFullName(String fullName) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(RepositorySchema.class)
                    .block();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return new RepositorySchema();
        }
    }

    public List<PullRequestSchema> findAllPullRequestByRepoName(String fullName) {
        return findAllPullRequestByRepoName(fullName, 100, "desc");
    }

    public List<PullRequestSchema> findAllPullRequestByRepoName(String fullName, int pageSize, String direction) {
        try {
            PullRequestSchema[] pullRequestSchemas = webClientBuilder.build()
                    .get()
                    .uri("https://api.github.com/repos/%s/pulls?state=all&per_page=%s&direction=%s".formatted(fullName, pageSize, direction))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(PullRequestSchema[].class)
                    .block();
            return pullRequestSchemas != null ? Arrays.asList(pullRequestSchemas) : List.of();
        } catch (WebClientResponseException exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<IssueSchema> findAllIssuesByRepoName(String fullName) {
        try {
            IssueSchema[] issueSchemas = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/issues?state=all&per_page=100&direction=desc", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(IssueSchema[].class)
                    .block();
            return issueSchemas != null ? Arrays.asList(issueSchemas) : List.of();
        } catch (WebClientResponseException exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<WeekOfCommitSchema> getCommitActivity(String fullName) {
        try {
            WeekOfCommitSchema[] commits = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/stats/commit_activity", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(WeekOfCommitSchema[].class)
                    .block();
            return commits != null ? Arrays.asList(commits) : List.of();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<ContributorSchema> findAllContributorsByRepoName(String fullName, int pageSize) {
        ContributorSchema[] contributorSchemas = null;
        try {
            contributorSchemas = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/contributors?per_page=%s", fullName, pageSize))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(ContributorSchema[].class)
                    .block();
        } catch (WebClientResponseException exception) {
            return List.of();
        }
        return contributorSchemas != null ? Arrays.asList(contributorSchemas) : List.of();
    }

    public List<ReleaseSchema> findAllReleasesByRepoName(String fullName, int pageSize) {
        ReleaseSchema[] releaseSchemas = null;
        try {
            releaseSchemas = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/releases?per_page=%s", fullName, pageSize))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(ReleaseSchema[].class)
                    .block();
        } catch (WebClientResponseException exception) {
            return List.of();
        }
        return releaseSchemas != null ? Arrays.asList(releaseSchemas) : List.of();
    }
}
