package com.goldengit.web.mapper;

import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.PullRequestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import static com.goldengit.domain.common.DateUtil.calculateCycleTime;

@Component
public class PullRequestResponseMapper extends ResponseMapper<PullRequestDTO, PullRequestResponse> {
    @Override
    public PullRequestResponse map(PullRequestDTO dto) {
        var response = PullRequestResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        if (dto.getClosedAt() != null && !dto.getClosedAt().isEmpty()) {
            response.setCycleTimeDays(
                    calculateCycleTime(dto.getCreatedAt(), dto.getClosedAt())
            );
        }
        return response;
    }
}
