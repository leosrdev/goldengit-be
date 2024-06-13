package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.ProjectDTO;
import com.goldengit.application.mapper.SchemaMapper;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProjectSchemaMapper extends SchemaMapper<GHRepository, ProjectDTO> {
    @Override
    public ProjectDTO map(GHRepository repository) {
        try {
            return ProjectDTO.builder()
                    .fullName(repository.getFullName())
                    .name(repository.getName())
                    .description(repository.getDescription())
                    .avatarUrl(repository.getOwner().getAvatarUrl())
                    .stars(repository.getStargazersCount())
                    .forks(repository.getForksCount())
                    .watchers(repository.getWatchersCount())
                    .defaultBranch(repository.getDefaultBranch())
                    .openIssues(repository.getOpenIssueCount())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
