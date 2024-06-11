package com.goldengit.application.service;

import com.goldengit.domain.model.Project;
import com.goldengit.infra.api.github.client.GitHubClient;
import com.goldengit.infra.api.github.schema.ReleaseSchema;
import com.goldengit.infra.api.openai.client.OpenAIClient;
import com.goldengit.web.model.ProjectSummaryResponse;
import com.goldengit.web.model.PullRequestResponse;
import com.goldengit.web.model.ReleaseResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectOverviewService extends BaseService {
    private final GitHubClient gitApi;
    private final ProjectService projectService;
    private final OpenAIClient openAiClient;

    @Cacheable(value = "ai-generation", key = "'projectSummary:' + #uuid")
    public ProjectSummaryResponse generateProjectSummary(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<PullRequestResponse> pullRequests = projectService.findPullRequestByRepoUuid(uuid);
        List<String> titles = pullRequests.stream().map(PullRequestResponse::getTitle).toList();
        String lastChanges = openAiClient.generateProjectSummaryFromPullRequests(project.getFullName(), titles);
        String description = openAiClient.generateProjectDescription(project.getFullName());

        return ProjectSummaryResponse.builder()
                .fullName(project.getFullName())
                .description(description)
                .lastChanges(lastChanges)
                .build();
    }

    @Cacheable(value = "git-repositories", key = "'releases:' + #uuid")
    public List<ReleaseResponse> findAllReleasesByRepo(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<ReleaseSchema> releaseSchemas = gitApi.findAllReleasesByRepoName(project.getFullName(), 10);

        return releaseSchemas.stream().parallel()
                .map(releaseSchema -> ReleaseResponse.builder()
                        .name(releaseSchema.name)
                        .tagName(releaseSchema.tag_name)
                        .htmlUrl(releaseSchema.html_url)
                        .userLogin(releaseSchema.author.login)
                        .userHtmlUrl(releaseSchema.author.html_url)
                        .userAvatarUrl(releaseSchema.author.avatar_url)
                        .publishedAt(releaseSchema.published_at)
                        .targetBranch(releaseSchema.target_commitish)
                        .draft(releaseSchema.draft)
                        .build()
                )
                .collect(Collectors.toList());
    }
}
