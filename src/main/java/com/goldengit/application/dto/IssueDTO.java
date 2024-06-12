package com.goldengit.application.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssueDTO {
    public long id;
    public int number;
    public String title;
    public String state;
    public String body;
    public String createdAt;
    public String closedAt;
}
