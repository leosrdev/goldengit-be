package com.goldengit.restclient.schema;

import lombok.Data;

@Data
public class WeekOfCommit {
    private long week;
    private int total;
}
