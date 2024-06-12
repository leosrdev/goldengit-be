package com.goldengit.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ResponseMapper<DTO, response> {
    protected abstract response map(DTO dto);

    public List<response> mapList(List<DTO> dtoList) {
        return dtoList.stream().map(this::map)
                .collect(Collectors.toList());
    }
}
