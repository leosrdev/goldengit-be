package com.goldengit.web.mapper;

import com.goldengit.application.dto.ProjectSummaryDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.ProjectSummaryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProjectSummaryResponseMapper extends ResponseMapper<ProjectSummaryDTO, ProjectSummaryResponse> {
    @Override
    public ProjectSummaryResponse map(ProjectSummaryDTO dto) {
        var response = ProjectSummaryResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
