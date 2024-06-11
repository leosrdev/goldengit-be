package com.goldengit.application.service;

import com.goldengit.domain.model.Project;
import com.goldengit.infra.api.github.client.GitHubClient;
import com.goldengit.infra.api.github.schema.ContributorSchema;
import com.goldengit.web.model.ContributorResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EngagementService extends BaseService {

    private final GitHubClient gitApi;
    private final ProjectService projectService;

    @Cacheable(value = "git-repositories", key = "'contributors:' + #uuid")
    public List<ContributorResponse> findTopContributorsByRepo(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<ContributorSchema> contributorSchemas = gitApi.findAllContributorsByRepoName(project.getFullName(), 10);

        return contributorSchemas.stream().parallel()
                .map(contributorSchema -> ContributorResponse.builder()
                        .id(contributorSchema.id)
                        .login(contributorSchema.login)
                        .avatarUrl(contributorSchema.avatar_url)
                        .htmlUrl(contributorSchema.html_url)
                        .contributions(contributorSchema.contributions)
                        .build()
                )
                .collect(Collectors.toList());
    }
}
