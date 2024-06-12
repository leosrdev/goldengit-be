package com.goldengit.infra.api.github.client;

import com.goldengit.application.dto.*;
import com.goldengit.application.service.ProjectDataSource;
import com.goldengit.infra.api.github.mapper.*;
import com.goldengit.infra.api.github.schema.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class GitHubClient implements ProjectDataSource {

    @Value("${github.api.token}")
    protected String apiToken;

    private final WebClient.Builder webClientBuilder;
    protected final static MediaType APPLICATION_JSON_GITHUB = MediaType.valueOf("application/vnd.github+json");

    private final ProjectSchemaMapper projectSchemaMapper;
    private final PullRequestSchemaMapper pullRequestSchemaMapper;
    private final IssueSchemaMapper issueSchemaMapper;
    private final WeekOfCommitSchemaMapper weekOfCommitSchemaMapper;
    private final ContributorSchemaMapper contributorSchemaMapper;
    private final ReleaseSchemaMapper releaseSchemaMapper;

    public List<ProjectDTO> findProjectsByQuery(String query) {
        try {
            var repositories = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/search/repositories?q=%s", query))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(RepositoriesSchema.class)
                    .block();

            return repositories != null ?
                    projectSchemaMapper.mapList(repositories.getItems()) :
                    List.of();

        } catch (Exception exception) {
            log.error(exception.getMessage(), exception.getCause());
            return List.of();
        }
    }

    public Optional<ProjectDTO> findRepoByFullName(String fullName) {
        try {
            var repository = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(RepositorySchema.class)
                    .block();
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
        return findAllPullRequestByRepoName(fullName, 100, "desc");
    }

    public List<PullRequestDTO> findAllPullRequestByRepoName(String fullName, int pageSize, String direction) {
        try {
            PullRequestSchema[] pullRequestSchemas = webClientBuilder.build()
                    .get()
                    .uri("https://api.github.com/repos/%s/pulls?state=all&per_page=%s&direction=%s".formatted(fullName, pageSize, direction))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(PullRequestSchema[].class)
                    .block();
            return pullRequestSchemas != null ? pullRequestSchemaMapper
                    .mapList(Arrays.asList(pullRequestSchemas)) : List.of();
        } catch (WebClientResponseException exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<IssueDTO> findAllIssuesByRepoName(String fullName) {
        try {
            IssueSchema[] issueSchemas = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/issues?state=all&per_page=100&direction=desc", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(IssueSchema[].class)
                    .block();

            return issueSchemas != null ?
                    issueSchemaMapper.mapList(Arrays.asList(issueSchemas)) :
                    List.of();
        } catch (WebClientResponseException exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<WeekOfCommitDTO> getCommitActivity(String fullName) {
        try {
            WeekOfCommitSchema[] weekOfCommits = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/stats/commit_activity", fullName))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(WeekOfCommitSchema[].class)
                    .block();
            return weekOfCommits != null ?
                    weekOfCommitSchemaMapper.mapList(Arrays.asList(weekOfCommits)) :
                    List.of();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<ContributorDTO> findAllContributorsByRepoName(String fullName, int pageSize) {
        try {
            ContributorSchema[] contributorSchemas = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/contributors?per_page=%s", fullName, pageSize))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(ContributorSchema[].class)
                    .block();

            return contributorSchemas != null ?
                    contributorSchemaMapper.mapList(Arrays.asList(contributorSchemas)) :
                    List.of();
        } catch (WebClientResponseException exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    public List<ReleaseDTO> findAllReleasesByRepoName(String fullName, int pageSize) {
        try {
            ReleaseSchema[] releaseSchemas = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/releases?per_page=%s", fullName, pageSize))
                    .accept(APPLICATION_JSON_GITHUB)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(ReleaseSchema[].class)
                    .block();
            return releaseSchemas != null ?
                    releaseSchemaMapper.mapList(Arrays.asList(releaseSchemas)) :
                    List.of();
        } catch (WebClientResponseException exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }
}
