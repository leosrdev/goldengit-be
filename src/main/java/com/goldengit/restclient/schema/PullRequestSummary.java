package com.goldengit.restclient.schema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PullRequestSummary {
    private String date;
    private Long total;
}
