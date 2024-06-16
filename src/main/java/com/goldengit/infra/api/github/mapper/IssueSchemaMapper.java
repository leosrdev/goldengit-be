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
                    //.body(issue.getBody())
                    .state(issue.getState().toString().toLowerCase())
                    .title(issue.getTitle())
                    .htmlUrl(issue.getHtmlUrl().toString())
                    .createdAt(dateFormat(issue.getCreatedAt()))
                    .closedAt(dateFormat(issue.getClosedAt()))
                    .userLogin(issue.getUser().getLogin())
                    .userName(issue.getUser().getName())
                    .userAvatarUrl(issue.getUser().getAvatarUrl())
                    .userHtmlUrl(issue.getUser().getHtmlUrl().toString())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
