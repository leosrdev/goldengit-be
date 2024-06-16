package com.goldengit.infra.api.github.mapper;

import com.goldengit.application.dto.PullRequestDTO;
import com.goldengit.application.dto.PullRequestReviewDTO;
import com.goldengit.application.mapper.SchemaMapper;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PullRequestReviewSchemaMapper extends SchemaMapper<GHPullRequestReview, PullRequestReviewDTO> {
    @Override
    public PullRequestReviewDTO map(GHPullRequestReview review) {
        try {
            return PullRequestReviewDTO.builder()
                    .userLogin(review.getUser().getLogin())
                    .userAvatarUrl(review.getUser().getAvatarUrl())
                    .userName(review.getUser().getName())
                    //.body(review.getBody())
                    .submittedAt(dateFormat(review.getSubmittedAt()))
                    .htmlUrl(review.getHtmlUrl().toString())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
