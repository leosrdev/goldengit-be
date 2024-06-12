package com.goldengit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContributorDTO {
    private long id;
    private String login;
    private String avatarUrl;
    private String htmlUrl;
    private long contributions;
}
