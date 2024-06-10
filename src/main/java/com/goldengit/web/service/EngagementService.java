package com.goldengit.web.service;

import com.goldengit.api.client.GitHubAPI;
import com.goldengit.api.schema.Contributor;
import com.goldengit.web.dto.ContributorResponse;
import com.goldengit.web.model.Project;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EngagementService extends BaseService {

    private final GitHubAPI gitApi;
    private final ProjectService projectService;

    @Cacheable(value = "git-repositories", key = "'contributors:' + #uuid")
    public List<ContributorResponse> findTopContributorsByRepo(String uuid) throws BadRequestException {
        Project project = projectService.getProjectByUUID(uuid);
        List<Contributor> contributors = gitApi.findAllContributorsByRepoName(project.getFullName(), 10);

        return contributors.stream().parallel()
                .map(contributor -> ContributorResponse.builder()
                        .id(contributor.id)
                        .login(contributor.login)
                        .avatarUrl(contributor.avatar_url)
                        .htmlUrl(contributor.html_url)
                        .contributions(contributor.contributions)
                        .build()
                )
                .collect(Collectors.toList());
    }
}
