package com.goldengit.web.service;

import com.goldengit.api.client.GitHubAPI;
import com.goldengit.api.schema.Release;
import com.goldengit.web.dto.ProjectSummaryResponse;
import com.goldengit.web.dto.PullRequestResponse;
import com.goldengit.web.dto.ReleaseResponse;
import com.goldengit.web.model.Project;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectOverviewService extends BaseService {
    private final GitHubAPI gitApi;
    private final ProjectService projectService;
    private final OpenAIService openAiService;

    @Cacheable(value = "ai-generation", key = "'projectSummary:' + #uuid")
    public ProjectSummaryResponse generateProjectSummary(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<PullRequestResponse> pullRequests = projectService.findPullRequestByRepoUuid(uuid);
        List<String> titles = pullRequests.stream().map(PullRequestResponse::getTitle).toList();
        String lastChanges = openAiService.generateProjectSummaryFromPullRequests(project.getFullName(), titles);
        String description = openAiService.generateProjectDescription(project.getFullName());

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
