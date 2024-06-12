package com.goldengit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseDTO {
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
