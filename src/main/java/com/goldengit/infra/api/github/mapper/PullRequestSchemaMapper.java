package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.mapper.SchemaMapper;
import org.kohsuke.github.GHPullRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PullRequestSchemaMapper extends SchemaMapper<GHPullRequest, PullRequestDTO> {
    @Override
    public PullRequestDTO map(GHPullRequest pullRequest) {
        try {
            return PullRequestDTO.builder()
                    .id(pullRequest.getId())
                    .number(pullRequest.getNumber())
                    .htmlUrl(pullRequest.getHtmlUrl().toString())
                    .title(pullRequest.getTitle())
                    .state(pullRequest.getState().toString().toLowerCase())
                    .createdAt(dateFormat(pullRequest.getCreatedAt()))
                    .closedAt(dateFormat(pullRequest.getClosedAt()))
                    //.body(pullRequest.body)
                    .userLogin(pullRequest.getUser().getLogin())
                    .userName(pullRequest.getUser().getName())
                    .userHtmlUrl(pullRequest.getUser().getHtmlUrl().toString())
                    .userAvatarUrl(pullRequest.getUser().getAvatarUrl())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
