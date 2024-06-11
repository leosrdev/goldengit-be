package com.goldengit.application.service;

import com.goldengit.domain.model.Project;
import com.goldengit.infra.api.github.client.GitHubClient;
import com.goldengit.infra.api.github.schema.Release;
import com.goldengit.infra.api.openai.client.OpenAIClient;
import com.goldengit.web.dto.ProjectSummaryResponse;
import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.ReleaseResponse;
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
        List<Release> releases = gitApi.findAllReleasesByRepoName(project.getFullName(), 10);

        return releases.stream().parallel()
                .map(release -> ReleaseResponse.builder()
                        .name(release.name)
                        .tagName(release.tag_name)
                        .htmlUrl(release.html_url)
                        .userLogin(release.author.login)
                        .userHtmlUrl(release.author.html_url)
                        .userAvatarUrl(release.author.avatar_url)
                        .publishedAt(release.published_at)
                        .targetBranch(release.target_commitish)
                        .draft(release.draft)
                        .build()
                )
                .collect(Collectors.toList());
    }
}
