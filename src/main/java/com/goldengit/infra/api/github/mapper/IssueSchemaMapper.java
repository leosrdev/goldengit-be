package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.IssueDTO;
import com.goldengit.application.mapper.SchemaMapper;
import org.kohsuke.github.GHIssue;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IssueSchemaMapper extends SchemaMapper<GHIssue, IssueDTO> {
    @Override
    public IssueDTO map(GHIssue issue) {
        try {
            return IssueDTO.builder()
                    .id(issue.getId())
                    .number(issue.getNumber())
                    .body(issue.getBody())
                    .state(issue.getState().toString().toLowerCase())
                    .title(issue.getTitle())
                    .createdAt(dateFormat(issue.getCreatedAt()))
                    .closedAt(dateFormat(issue.getClosedAt()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
