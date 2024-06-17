package com.goldengit.web.mapper;

import com.goldengit.application.dto.ReviewerDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.ReviewerResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ReviewerResponseMapper extends
        ResponseMapper<ReviewerDTO, ReviewerResponse> {
    @Override
    public ReviewerResponse map(ReviewerDTO dto) {
        var response = ReviewerResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
