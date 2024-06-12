package com.goldengit.web.mapper;

import com.goldengit.application.dto.WeekOfCommitDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.WeekOfCommitResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class WeekOfCommitResponseMapper extends ResponseMapper<WeekOfCommitDTO, WeekOfCommitResponse> {
    @Override
    public WeekOfCommitResponse map(WeekOfCommitDTO dto) {
        var response = WeekOfCommitResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
