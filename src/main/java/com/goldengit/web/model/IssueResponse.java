package com.goldengit.web.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssueResponse {
    public long id;
    public int number;
    public String title;
    public String htmlUrl;
    public String state;
    public String createdAt;
    public String closedAt;
    private String userLogin;
    private String userName;
    private String userHtmlUrl;
    private String userAvatarUrl;
    private float cycleTimeDays;
}
