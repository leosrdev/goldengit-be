package com.goldengit.restclient.schema;

import lombok.Data;

import java.util.List;

@Data
public class Repositories {
    private List<Repository> items;
}
