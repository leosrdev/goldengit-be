package com.goldengit.restclient.service;

import com.goldengit.restclient.api.GitHubAPI;
import com.goldengit.restclient.schema.Contributor;
import com.goldengit.restclient.schema.Release;
import com.goldengit.web.dto.ContributorResponse;
import com.goldengit.web.dto.ReleaseResponse;
import com.goldengit.web.model.GitProject;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OverviewService extends BaseService{
    private final GitHubAPI gitApi;

    @Cacheable(value = "git-repositories", key = "'releases:' + #uuid")
    public List<ReleaseResponse> findAllReleasesByRepo(String uuid) throws BadRequestException {
        GitProject project = getProjectByUUID(uuid);
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
