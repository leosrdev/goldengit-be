package com.goldengit.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContributorResponse {
    private long id;
    private String login;
    private String avatarUrl;
    private String htmlUrl;
    private long contributions;
}
