package com.goldengit.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PullRequestResponse {
    private long id;
    private int number;
    private String createdAt;
    private String closedAt;
    private String title;
    private String state;
    private String body;
    private String userLogin;
    private String userHtmlUrl;
    private String userAvatarUrl;
}
