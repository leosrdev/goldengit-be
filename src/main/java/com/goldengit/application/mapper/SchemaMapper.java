package com.goldengit.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SchemaMapper<ApiSchema, DTO> {
    protected abstract DTO map(ApiSchema schema);

    public List<DTO> mapList(List<ApiSchema> sourceList) {
        return sourceList.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
