package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.mapper.SchemaMapper;
import org.kohsuke.github.GHPullRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PullRequestSchemaMapper extends SchemaMapper<GHPullRequest, PullRequestDTO> {
    @Override
    public PullRequestDTO map(GHPullRequest schema) {
        try {
            return PullRequestDTO.builder()
                    .id(schema.getId())
                    .number(schema.getNumber())
                    .htmlUrl(schema.getHtmlUrl().toString())
                    .title(schema.getTitle())
                    .state(schema.getState().toString().toLowerCase())
                    .createdAt(dateFormat(schema.getCreatedAt()))
                    .closedAt(dateFormat(schema.getClosedAt()))
                    //.body(pullRequest.body)
                    .userLogin(schema.getUser().getLogin())
                    .userHtmlUrl(schema.getUser().getHtmlUrl().toString())
                    .userAvatarUrl(schema.getUser().getAvatarUrl())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
