package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.ReleaseDTO;
import com.goldengit.application.mapper.SchemaMapper;
import org.kohsuke.github.GHRelease;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReleaseSchemaMapper extends SchemaMapper<GHRelease, ReleaseDTO> {
    @Override
    protected ReleaseDTO map(GHRelease release) {
        try {
            return ReleaseDTO.builder()
                    .name(release.getName())
                    .tagName(release.getTagName())
                    .htmlUrl(release.getHtmlUrl().toString())
                    .assetsUrl(release.getAssetsUrl())
                    .createdAt(dateFormat(release.getCreatedAt()))
                    .publishedAt(dateFormat(release.getPublished_at()))
                    .targetBranch(release.getTargetCommitish())
                    .draft(release.isDraft())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
