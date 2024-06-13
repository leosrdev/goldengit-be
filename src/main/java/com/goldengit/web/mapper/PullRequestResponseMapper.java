package com.goldengit.web.mapper;

import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.PullRequestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.goldengit.domain.common.DateUtil.DATE_FORMAT_UTC;

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

    private float calculateCycleTime(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_UTC);
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);
        long hours = ChronoUnit.HOURS.between(startDateTime, endDateTime);
        float days = (float) hours / 24;
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(days));
    }
}
