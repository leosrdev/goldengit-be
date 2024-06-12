package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.ReleaseDTO;
import com.goldengit.application.mapper.SchemaMapper;
import com.goldengit.infra.api.github.schema.ReleaseSchema;
import org.springframework.stereotype.Component;

@Component
public class ReleaseSchemaMapper extends SchemaMapper<ReleaseSchema, ReleaseDTO> {
    @Override
    protected ReleaseDTO map(ReleaseSchema releaseSchema) {
        return ReleaseDTO.builder()
                .name(releaseSchema.name)
                .tagName(releaseSchema.tag_name)
                .htmlUrl(releaseSchema.html_url)
                .userLogin(releaseSchema.author.login)
                .userHtmlUrl(releaseSchema.author.html_url)
                .userAvatarUrl(releaseSchema.author.avatar_url)
                .publishedAt(releaseSchema.published_at)
                .targetBranch(releaseSchema.target_commitish)
                .draft(releaseSchema.draft)
                .build();
    }
}
