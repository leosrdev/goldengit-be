package com.goldengit.application.service;

import com.goldengit.domain.model.Project;
import com.goldengit.infra.api.github.client.GitHubClient;
import com.goldengit.infra.api.github.schema.PullRequestSchema;
import com.goldengit.infra.api.github.schema.RepositoriesSchema;
import com.goldengit.infra.api.github.schema.RepositorySchema;
import com.goldengit.infra.db.ProjectRepository;
import com.goldengit.web.model.PullRequestResponse;
import com.goldengit.web.model.RepoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService extends BaseService {

    private final GitHubClient gitApi;
    private final ProjectRepository repository;

    @Cacheable(value = "git-repositories", key = "'projects:' + #uuid")
    protected Project getProjectByUUID(String uuid) throws BadRequestException {
        return findById(uuid)
                .orElseThrow(() -> {
                    log.error("No project found with uuid: " + uuid);
                    return new BadRequestException("No project found with uuid: " + uuid);
                });
    }

    @Cacheable("git-repositories")
    public Project findOrCreate(String fullName) {
        Optional<Project> optionalGitProject = repository.findByFullName(fullName);
        return optionalGitProject.orElseGet(() -> repository.save(
                Project.builder()
                        .uuid(UUID.randomUUID().toString())
                        .fullName(fullName)
                        .build()
        ));
    }

    public Optional<Project> findById(String uuid) {
        return repository.findById(uuid);
    }

    @Cacheable("git-repositories")
    public List<RepoResponse> findRepoByQuery(String query) {
        RepositoriesSchema repositoriesSchema = gitApi.findRepoByQuery(query);

        return repositoriesSchema.getItems().stream().map(repositorySchema ->
                RepoResponse.builder()
                        .fullName(repositorySchema.full_name)
                        .description(repositorySchema.description)
                        .stars(repositorySchema.stargazers_count)
                        .forks(repositorySchema.forks_count)
                        .watchers(repositorySchema.watchers_count)
                        .defaultBranch(repositorySchema.default_branch)
                        .openIssues(repositorySchema.open_issues_count)
                        .build()
        ).collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'pullRequests:' + #uuid")
    public List<PullRequestResponse> findPullRequestByRepoUuid(String uuid) throws BadRequestException {
        Project project = getProjectByUUID(uuid);
        List<PullRequestSchema> pullRequestSchemas = gitApi.findAllPullRequestByRepoName(project.getFullName(), 15, "desc");
        return pullRequestSchemas.stream().map(pullRequestSchema ->
                PullRequestResponse.builder()
                        .id(pullRequestSchema.id)
                        .number(pullRequestSchema.number)
                        .htmlUrl(pullRequestSchema.html_url)
                        .title(pullRequestSchema.title)
                        .state(pullRequestSchema.state)
                        .createdAt(pullRequestSchema.created_at)
                        .closedAt(pullRequestSchema.closed_at)
                        //.body(pullRequest.body)
                        .userLogin(pullRequestSchema.user.login)
                        .userHtmlUrl(pullRequestSchema.user.html_url)
                        .userAvatarUrl(pullRequestSchema.user.avatar_url)
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
                "ollama/ollama",
                "django/django",
                "pytorch/pytorch",
                "tensorflow/tensorflow",
                "vitejs/vite",
                "apache/spark",
                "apache/kafka",
                "grafana/grafana",
                "open-webui/open-webui",
                "mysql/mysql-server",
                "mongodb/mongo"
        };

        List<RepositorySchema> repos = Stream.of(popularRepositories).parallel()
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
                .map(repositorySchema -> {
                    Project gitProject = findOrCreate(repositorySchema.full_name);
                    return RepoResponse.builder()
                            .uuid(gitProject.getUuid())
                            .name(repositorySchema.name)
                            .fullName(repositorySchema.full_name)
                            .description(repositorySchema.description)
                            .avatarUrl(repositorySchema.owner.avatar_url)
                            .stars(repositorySchema.stargazers_count)
                            .forks(repositorySchema.forks_count)
                            .watchers(repositorySchema.watchers_count)
                            .defaultBranch(repositorySchema.default_branch)
                            .openIssues(repositorySchema.open_issues_count)
                            .build();
                })
                .sorted((r1, r2) -> Integer.compare(r2.getStars(), r1.getStars()))
                .collect(Collectors.toList());
    }
}
