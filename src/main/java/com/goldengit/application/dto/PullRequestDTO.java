package com.goldengit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PullRequestDTO {
    private long id;
    private int number;
    private String htmlUrl;
    private String createdAt;
    private String closedAt;
    private String title;
    private String state;
    private String body;
    private String userLogin;
    private String userHtmlUrl;
    private String userAvatarUrl;
}
