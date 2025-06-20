package com.goldengit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDTO {
    public long id;
    public int number;
    public String title;
    public String htmlUrl;
    public String state;
    public String body;
    public String createdAt;
    public String closedAt;
    private String userLogin;
    private String userName;
    private String userHtmlUrl;
    private String userAvatarUrl;
}
