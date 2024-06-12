package com.goldengit.web.mapper;

import com.goldengit.application.dto.ProjectDTO;
import com.goldengit.application.mapper.ResponseMapper;
import com.goldengit.web.model.ProjectResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProjectResponseMapper extends ResponseMapper<ProjectDTO, ProjectResponse> {
    @Override
    public ProjectResponse map(ProjectDTO dto) {
        var response = ProjectResponse.builder().build();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
}
