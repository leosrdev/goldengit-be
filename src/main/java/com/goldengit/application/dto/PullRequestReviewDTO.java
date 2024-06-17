package com.goldengit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PullRequestReviewDTO {
    private long pullRequestId;
    private int pullRequestNumber;
    private String userLogin;
    private String userName;
    private String userAvatarUrl;
    private String userHtmlUrl;
    private String body;
    private String submittedAt;
    private String htmlUrl;
}
