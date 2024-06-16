package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.ContributorDTO;
import com.goldengit.application.mapper.SchemaMapper;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ContributorSchemaMapper extends SchemaMapper<GHRepository.Contributor, ContributorDTO> {
    @Override
    protected ContributorDTO map(GHRepository.Contributor contributor) {
        try {
            return ContributorDTO.builder()
                    .id(contributor.getId())
                    .login(contributor.getLogin())
                    .name(contributor.getName())
                    .avatarUrl(contributor.getAvatarUrl())
                    .htmlUrl(contributor.getHtmlUrl().toString())
                    .contributions(contributor.getContributions())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
