package com.goldengit.web.mapper;

import com.goldengit.application.dto.PullRequestReviewDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.PullRequestReviewResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PullRequestReviewResponseMapper extends ResponseMapper<PullRequestReviewDTO, PullRequestReviewResponse> {
    @Override
    public PullRequestReviewResponse map(PullRequestReviewDTO dto) {
        var response = PullRequestReviewResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
