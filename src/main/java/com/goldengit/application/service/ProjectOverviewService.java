package com.goldengit.application.service;

import com.goldengit.application.dto.ProjectSummaryDTO;
import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.dto.ReleaseDTO;
import com.goldengit.domain.model.Project;
import com.goldengit.infra.api.openai.client.OpenAIClient;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectOverviewService extends BaseService {
    private final ProjectDataSource projectDataSource;
    private final ProjectService projectService;
    private final OpenAIClient openAiClient;

    @Cacheable(value = "ai-generation", key = "'projectSummary:' + #uuid")
    public ProjectSummaryDTO generateProjectSummary(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<PullRequestDTO> pullRequests = projectDataSource.findMergedPullRequestByRepoName(project.getFullName(), 20);
        List<String> titles = pullRequests.stream().map(PullRequestDTO::getTitle).toList();
        String lastChanges = openAiClient.generateProjectSummaryFromPullRequests(project.getFullName(), titles);
        String description = openAiClient.generateProjectDescription(project.getFullName());

        return ProjectSummaryDTO.builder()
                .fullName(project.getFullName())
                .description(description)
                .lastChanges(lastChanges)
                .build();
    }

    @Cacheable(value = "git-repositories", key = "'releases:' + #uuid")
    public List<ReleaseDTO> findAllReleasesByRepo(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        return projectDataSource.findAllReleasesByRepoName(project.getFullName(), 10);
    }
}
