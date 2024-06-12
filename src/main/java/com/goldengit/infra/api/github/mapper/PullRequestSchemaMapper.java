package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.mapper.SchemaMapper;
import com.goldengit.infra.api.github.schema.PullRequestSchema;
import org.springframework.stereotype.Component;

@Component
public class PullRequestSchemaMapper extends SchemaMapper<PullRequestSchema, PullRequestDTO> {
    @Override
    public PullRequestDTO map(PullRequestSchema schema) {
        return PullRequestDTO.builder()
                .id(schema.id)
                .number(schema.number)
                .htmlUrl(schema.html_url)
                .title(schema.title)
                .state(schema.state)
                .createdAt(schema.created_at)
                .closedAt(schema.closed_at)
                //.body(pullRequest.body)
                .userLogin(schema.user.login)
                .userHtmlUrl(schema.user.html_url)
                .userAvatarUrl(schema.user.avatar_url)
                .build();
    }
}
