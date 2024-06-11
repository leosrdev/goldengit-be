package com.goldengit.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSummaryResponse {
    private String fullName;
    private String description;
    private String lastChanges;
}
