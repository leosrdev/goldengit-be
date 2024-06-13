package com.goldengit.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.kohsuke.github.GHIssueState;

@Data
@Builder
@AllArgsConstructor
public final class FindIssueQuery {

    @NonNull
    private String repositoryName;
    private GHIssueState state;
    int maxResults;
}
