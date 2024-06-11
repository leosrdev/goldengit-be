package com.goldengit.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseResponse {
    private String name;
    private String tagName;
    private String htmlUrl;
    private String userLogin;
    private String userHtmlUrl;
    private String userAvatarUrl;
    private String publishedAt;
    private String targetBranch;
    private String draft;
}
