package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.WeekOfCommitDTO;
import com.goldengit.application.mapper.SchemaMapper;
import org.kohsuke.github.GHRepositoryStatistics;
import org.springframework.stereotype.Component;

@Component
public class WeekOfCommitSchemaMapper extends SchemaMapper<GHRepositoryStatistics.CommitActivity, WeekOfCommitDTO> {
    @Override
    protected WeekOfCommitDTO map(GHRepositoryStatistics.CommitActivity commitActivity) {
        return WeekOfCommitDTO.builder()
                .week(commitActivity.getWeek())
                .total(commitActivity.getTotal())
                .build();
    }
}
