package com.goldengit.application.service;

import com.goldengit.application.dto.ContributorDTO;
import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.domain.model.Project;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
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

    public List<PullRequestDTO> calculatePullRequestsCycleTime(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        var pullRequests = projectDataSource.findMergedPullRequestByRepoName(project.getFullName(), 50);
        return pullRequests.stream().parallel()
                .sorted(Comparator.comparingInt(PullRequestDTO::getNumber))
                .collect(Collectors.toList());
    }
}
