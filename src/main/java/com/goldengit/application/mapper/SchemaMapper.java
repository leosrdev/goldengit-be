package com.goldengit.application.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SchemaMapper<ApiSchema, DTO> {
    protected abstract DTO map(ApiSchema schema);

    public List<DTO> mapList(List<ApiSchema> sourceList) {
        return sourceList.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    protected String dateFormat(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return formatter.format(date);
    }
}
