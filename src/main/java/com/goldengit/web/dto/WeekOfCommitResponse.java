package com.goldengit.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WeekOfCommitResponse {
    private long week;
    private int total;
}
