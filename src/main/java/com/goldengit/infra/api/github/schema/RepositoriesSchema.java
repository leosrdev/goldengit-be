package com.goldengit.infra.api.github.schema;

import lombok.Data;

import java.util.List;

@Data
public class RepositoriesSchema {
    private List<RepositorySchema> items;
}
