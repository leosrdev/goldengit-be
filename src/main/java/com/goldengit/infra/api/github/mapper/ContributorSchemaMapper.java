package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.ContributorDTO;
import com.goldengit.application.mapper.SchemaMapper;
import com.goldengit.infra.api.github.schema.ContributorSchema;
import org.springframework.stereotype.Component;

@Component
public class ContributorSchemaMapper extends SchemaMapper<ContributorSchema, ContributorDTO> {
    @Override
    protected ContributorDTO map(ContributorSchema contributorSchema) {
        return ContributorDTO.builder()
                .id(contributorSchema.id)
                .login(contributorSchema.login)
                .avatarUrl(contributorSchema.avatar_url)
                .htmlUrl(contributorSchema.html_url)
                .contributions(contributorSchema.contributions)
                .build();
    }
}
