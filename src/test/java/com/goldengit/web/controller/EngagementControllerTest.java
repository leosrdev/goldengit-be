package com.goldengit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldengit.application.dto.ContributorDTO;
import com.goldengit.application.service.EngagementService;
import com.goldengit.infra.config.WebConfig;
import com.goldengit.web.mapper.ContributorResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = {
        WebConfig.class,
        EngagementService.class,
        ContributorResponseMapper.class,
        EngagementController.class,
        ObjectMapper.class
})
@AutoConfigureMockMvc
public class EngagementControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private EngagementController engagementController;

    @MockBean
    private EngagementService engagementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ContributorResponseMapper contributorResponseMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(engagementController)
                .build();
    }

    @Test
    public void shouldGetRepositoryContributors() throws Exception {
        String uuid = "sample";
        ContributorDTO contributorDTO = ContributorDTO.builder().id(1).login("John").build();
        var contributorResponse = contributorResponseMapper.map(contributorDTO);

        when(engagementService.findTopContributorsByRepo(uuid))
                .thenReturn(List.of(contributorDTO));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/repos/%s/engagement/contributors".formatted(uuid))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(contributorResponse)));
    }
}
