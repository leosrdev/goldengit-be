package com.goldengit.web.mapper;

import com.goldengit.application.dto.IssueSummaryDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.IssueSummaryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class IssueSummaryResponseMapper extends ResponseMapper<IssueSummaryDTO, IssueSummaryResponse> {
    @Override
    public IssueSummaryResponse map(IssueSummaryDTO dto) {
        var response = IssueSummaryResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
