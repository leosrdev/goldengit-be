package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.ProjectDTO;
import com.goldengit.application.mapper.SchemaMapper;
import com.goldengit.infra.api.github.schema.RepositorySchema;
import org.springframework.stereotype.Component;

@Component
public class ProjectSchemaMapper extends SchemaMapper<RepositorySchema, ProjectDTO> {
    @Override
    public ProjectDTO map(RepositorySchema schema) {
        return ProjectDTO.builder()
                .fullName(schema.full_name)
                .name(schema.name)
                .description(schema.description)
                .avatarUrl(schema.owner.avatar_url)
                .stars(schema.stargazers_count)
                .forks(schema.forks_count)
                .watchers(schema.watchers_count)
                .defaultBranch(schema.default_branch)
                .openIssues(schema.open_issues_count)
                .build();
    }
}
