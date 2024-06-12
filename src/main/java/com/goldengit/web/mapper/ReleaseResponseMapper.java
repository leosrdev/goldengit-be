package com.goldengit.web.mapper;

import com.goldengit.application.dto.ReleaseDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.ReleaseResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ReleaseResponseMapper extends ResponseMapper<ReleaseDTO, ReleaseResponse> {
    @Override
    public ReleaseResponse map(ReleaseDTO dto) {
        var response = ReleaseResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
