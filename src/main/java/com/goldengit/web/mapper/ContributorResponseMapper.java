package com.goldengit.web.mapper;

import com.goldengit.application.dto.ContributorDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.ContributorResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ContributorResponseMapper extends ResponseMapper<ContributorDTO, ContributorResponse> {
    @Override
    public ContributorResponse map(ContributorDTO dto) {
        var response = ContributorResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
