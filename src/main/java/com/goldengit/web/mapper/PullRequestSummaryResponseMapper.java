package com.goldengit.web.mapper;

import com.goldengit.application.dto.PullRequestSummaryDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.PullRequestSummaryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PullRequestSummaryResponseMapper extends ResponseMapper<PullRequestSummaryDTO, PullRequestSummaryResponse> {
    @Override
    public PullRequestSummaryResponse map(PullRequestSummaryDTO dto) {
        var response = PullRequestSummaryResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
