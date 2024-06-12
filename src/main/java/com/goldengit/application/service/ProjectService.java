package com.goldengit.application.service;

import com.goldengit.application.dto.ProjectDTO;
import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.domain.model.Project;
import com.goldengit.infra.db.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService extends BaseService {

    private final ProjectDataSource projectDataSource;
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
    public List<ProjectDTO> findRepoByQuery(String query) {
        return projectDataSource.findProjectsByQuery(query);
    }

    @Cacheable(value = "git-repositories", key = "'pullRequests:' + #uuid")
    public List<PullRequestDTO> findPullRequestByRepoUuid(String uuid) throws BadRequestException {
        Project project = getProjectByUUID(uuid);
        return projectDataSource.findAllPullRequestByRepoName(project.getFullName(), 15, "desc");
    }

    @Cacheable(value = "git-repositories", key = "'popularRepositories'")
    public List<ProjectDTO> listPopularProjects() {
        List<String> popularRepositories = getPopularProjectsList();
        List<Optional<ProjectDTO>> projects = popularRepositories
                .stream()
                .parallel()
                .map(projectDataSource::findRepoByFullName)
                .toList();

        return projects.stream()
                .filter(Optional::isPresent)
                .map(project -> {
                    Project gitProject = findOrCreate(project.get().getFullName());
                    project.get().setUuid(gitProject.getUuid());
                    return project.get();
                })
                .sorted((r1, r2) -> Integer.compare(r2.getStars(), r1.getStars()))
                .collect(Collectors.toList());
    }

    private List<String> getPopularProjectsList() {
        var projects = new String[]{
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
        return Arrays.asList(projects);
    }
}
