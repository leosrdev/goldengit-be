package com.goldengit.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PullRequestReviewResponse {
    private long pullRequestId;
    private int pullRequestNumber;
    private String userLogin;
    private String userName;
    private String userAvatarUrl;
    private String body;
    private String submittedAt;
    private String htmlUrl;
}
