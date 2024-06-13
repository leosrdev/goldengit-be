package com.goldengit.web.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssueResponse {
    public long id;
    public int number;
    public String title;
    public String state;
    public String body;
    public String createdAt;
    public String closedAt;
    private float cycleTimeDays;
}
