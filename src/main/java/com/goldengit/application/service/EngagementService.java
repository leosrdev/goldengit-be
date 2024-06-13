package com.goldengit.application.service;

import com.goldengit.application.dto.ContributorDTO;
import com.goldengit.application.dto.IssueDTO;
import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.query.FindIssueQuery;
import com.goldengit.domain.model.Project;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.kohsuke.github.GHIssueState;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EngagementService extends BaseService {
    private final ProjectDataSource projectDataSource;
    private final ProjectService projectService;

    @Cacheable(value = "git-repositories", key = "'contributors:' + #uuid")
    public List<ContributorDTO> findTopContributorsByRepo(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        return projectDataSource.findAllContributorsByRepoName(project.getFullName(), 10);
    }

    @Cacheable(value = "git-repositories", key = "'pull-cycle:' + #uuid")
    public List<PullRequestDTO> findMergedPullRequests(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        var pullRequests = projectDataSource.findMergedPullRequestByRepoName(project.getFullName(), 100);
        return pullRequests.stream().parallel()
                .sorted(Comparator.comparingInt(PullRequestDTO::getNumber))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "git-repositories", key = "'issue-cycle:' + #uuid")
    public List<IssueDTO> findSolvedIssues(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        var issues = projectDataSource.findIssues(
                new FindIssueQuery(project.getFullName(), GHIssueState.CLOSED, 100)
        );
        return issues.stream().parallel()
                .sorted(Comparator.comparingInt(IssueDTO::getNumber))
                .collect(Collectors.toList());
    }
}
