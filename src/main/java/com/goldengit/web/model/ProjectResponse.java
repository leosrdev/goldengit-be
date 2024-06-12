package com.goldengit.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponse {
    private String uuid;
    private String name;
    private String fullName;
    private String description;
    private String avatarUrl;
    private int stars;
    private int forks;
    private int watchers;
    private int openIssues;
    private String defaultBranch;
}
