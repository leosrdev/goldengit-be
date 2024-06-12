package com.goldengit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSummaryDTO {
    private String fullName;
    private String description;
    private String lastChanges;
}
