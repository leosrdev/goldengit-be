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
    private String assetsUrl;
    private String createdAt;
    private String publishedAt;
    private String targetBranch;
    private Boolean draft;
}
