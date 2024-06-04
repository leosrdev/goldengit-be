package com.goldengit.restclient.service;

import com.goldengit.restclient.api.GitHubAPI;
import com.goldengit.restclient.schema.PullRequest;
import com.goldengit.restclient.schema.Repositories;
import com.goldengit.restclient.schema.Repository;
import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.RepoResponse;
import com.goldengit.web.model.GitProject;
import com.goldengit.web.service.GitProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitService extends BaseService {

    private final GitHubAPI gitApi;
    private final GitProjectService gitProjectService;

    @Cacheable("git-repositories")
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

    public List<PullRequestResponse> findPullRequestByRepoUuid(String uuid) throws BadRequestException {
        GitProject project = getProjectByUUID(uuid);
        List<PullRequest> pullRequests = gitApi.findAllPullRequestByRepoName(project.getFullName(), 30, "desc");
        return pullRequests.stream().map(pullRequest ->
                PullRequestResponse.builder()
                        .id(pullRequest.id)
                        .number(pullRequest.number)
                        .title(pullRequest.title)
                        .state(pullRequest.state)
                        .createdAt(pullRequest.created_at)
                        .closedAt(pullRequest.closed_at)
                        //.body(pullRequest.body)
                        .userLogin(pullRequest.user.login)
                        .userHtmlUrl(pullRequest.user.html_url)
                        .userAvatarUrl(pullRequest.user.avatar_url)
                        .build()
        ).collect(Collectors.toList());
    }


    @Cacheable(value = "git-repositories", key = "'popularRepositories'")
    public List<RepoResponse> listPopularRepositories() {
        String[] popularRepositories = new String[]{
                "twbs/bootstrap",
                "microsoft/TypeScript",
                "facebook/react",
                "vercel/next.js",
                "spring-projects/spring-boot",
                "nodejs/node",
                "expressjs/express",
                "angular/angular",
                "php/php-src",
                "laravel/laravel",
                "django/django",
                "pytorch/pytorch",
                "tensorflow/tensorflow",
                "vitejs/vite",
                "apache/spark",
                "apache/kafka",
                "grafana/grafana",
                "apache/maven",
                "mysql/mysql-server",
                "mongodb/mongo"
        };

        List<Repository> repos = Stream.of(popularRepositories).parallel()
                .map(name -> {
                    try {
                        return gitApi.findRepoByFullName(name);
                    } catch (Exception exception) {
                        log.error("Error when fetching data, repository: " + name + ", error: " + exception.getMessage());
                        return null;
                    }
                }).toList();
        return repos.stream()
                .filter(Objects::nonNull)
                .map(repository -> {
                    GitProject gitProject = gitProjectService.findOrCreate(repository.full_name);
                    return RepoResponse.builder()
                            .uuid(gitProject.getUuid())
                            .name(repository.name)
                            .fullName(repository.full_name)
                            .description(repository.description)
                            .avatarUrl(repository.owner.avatar_url)
                            .stars(repository.stargazers_count)
                            .forks(repository.forks_count)
                            .watchers(repository.watchers_count)
                            .defaultBranch(repository.default_branch)
                            .openIssues(repository.open_issues_count)
                            .build();
                })
                .sorted((r1, r2) -> Integer.compare(r2.getStars(), r1.getStars()))
                .collect(Collectors.toList());
    }
}
