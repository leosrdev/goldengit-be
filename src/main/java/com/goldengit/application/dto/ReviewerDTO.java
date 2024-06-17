package com.goldengit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewerDTO {
    private String login;
    private String name;
    private String avatarUrl;
    private String htmlUrl;
    private Long reviews;
}
