package com.goldengit.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewerResponse {
    private String login;
    private String name;
    private String avatarUrl;
    private String htmlUrl;
    private Long reviews;
}
