package com.goldengit.application.service;

import com.goldengit.application.dto.IssueDTO;
import com.goldengit.application.dto.ProjectDTO;
import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.query.FindIssueQuery;
import com.goldengit.domain.model.Project;
import com.goldengit.infra.db.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.kohsuke.github.GHIssueState;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService extends BaseService {

    private final ProjectDataSource projectDataSource;
    private final ProjectRepository projectRepository;

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
        Optional<Project> optionalGitProject = projectRepository.findByFullName(fullName);
        return optionalGitProject.orElseGet(() -> projectRepository.save(
                Project.builder()
                        .uuid(UUID.randomUUID().toString())
                        .fullName(fullName)
                        .build()
        ));
    }

    public Optional<Project> findById(String uuid) {
        return projectRepository.findById(uuid);
    }

    @Cacheable("git-repositories")
    public List<ProjectDTO> findRepoByQuery(String query) {
        return projectDataSource.findProjectsByQuery(query);
    }

    @Cacheable(value = "git-repositories", key = "'pullRequests:' + #uuid")
    public List<PullRequestDTO> findLatestPullRequestByRepoUuid(String uuid) throws BadRequestException {
        Project project = getProjectByUUID(uuid);
        return projectDataSource.findAllPullRequestByRepoName(project.getFullName(), 10);
    }

    @Cacheable(value = "git-repositories", key = "'issues:' + #uuid")
    public List<IssueDTO> findLatestIssues(String uuid) throws BadRequestException {
        Project project = getProjectByUUID(uuid);
        var issues = projectDataSource.findIssues(
                new FindIssueQuery(project.getFullName(), GHIssueState.OPEN, 10)
        );
        return issues.stream().parallel()
                .sorted(Comparator.comparingInt(IssueDTO::getNumber))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'popularRepositories'")
    public List<ProjectDTO> listPopularProjects() {
        var popularProjects = projectRepository.findAll();
        var projects = StreamSupport.stream(popularProjects.spliterator(), false)
                .parallel()
                .map(project -> projectDataSource.findRepoByFullName(project.getFullName()))
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
}
