package com.goldengit.application.service;

import com.goldengit.application.dto.ContributorDTO;
import com.goldengit.domain.model.Project;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
