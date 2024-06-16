package com.goldengit.web.mapper;

import com.goldengit.application.dto.PullRequestReviewSummaryDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.PullRequestReviewSummaryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PullRequestReviewSummaryResponseMapper extends
        ResponseMapper<PullRequestReviewSummaryDTO, PullRequestReviewSummaryResponse> {
    @Override
    public PullRequestReviewSummaryResponse map(PullRequestReviewSummaryDTO dto) {
        var response = PullRequestReviewSummaryResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
