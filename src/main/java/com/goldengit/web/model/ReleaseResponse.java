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
    private String assetsUrl;
    private String createdAt;
    private String publishedAt;
    private String targetBranch;
    private Boolean draft;
}
