package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.IssueDTO;
import com.goldengit.application.mapper.SchemaMapper;
import com.goldengit.infra.api.github.schema.IssueSchema;
import org.springframework.stereotype.Component;

@Component
public class IssueSchemaMapper extends SchemaMapper<IssueSchema, IssueDTO> {
    @Override
    public IssueDTO map(IssueSchema schema) {
        return IssueDTO.builder()
                .id(schema.id)
                .number(schema.number)
                .body(schema.body)
                .state(schema.state)
                .title(schema.title)
                .createdAt(schema.created_at)
                .closedAt(schema.closed_at)
                .build();
    }
}
