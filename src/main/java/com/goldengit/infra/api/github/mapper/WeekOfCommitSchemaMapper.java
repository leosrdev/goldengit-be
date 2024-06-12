package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.WeekOfCommitDTO;
import com.goldengit.application.mapper.SchemaMapper;
import com.goldengit.infra.api.github.schema.WeekOfCommitSchema;
import org.springframework.stereotype.Component;

@Component
public class WeekOfCommitSchemaMapper extends SchemaMapper<WeekOfCommitSchema, WeekOfCommitDTO> {
    @Override
    protected WeekOfCommitDTO map(WeekOfCommitSchema schema) {
        return WeekOfCommitDTO.builder()
                .week(schema.week)
                .total(schema.total)
                .build();
    }
}
