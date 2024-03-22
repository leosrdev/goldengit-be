package com.goldengit.restclient.repository;

import com.goldengit.restclient.schema.PullRequest;
import com.goldengit.restclient.schema.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GitRepository {

    @Value("${github.api.token}")
    private String apiToken;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Repositories findRepoByQuery(String query) {
        return webClientBuilder.build()
                .get()
                .uri(String.format("https://api.github.com/search/repositories?q=%s", query))
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                .retrieve()
                .bodyToMono(Repositories.class)
                .block();
    }

    public List<PullRequest> findPullRequestByRepoName(String fullName) {
        PullRequest[] pullRequests = null;
        try {
            pullRequests = webClientBuilder.build()
                    .get()
                    .uri(String.format("https://api.github.com/repos/%s/pulls", fullName))
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                    .retrieve()
                    .bodyToMono(PullRequest[].class)
                    .block();
        } catch (WebClientResponseException exception) {
            return new ArrayList<>();
        }

        return Arrays.asList(pullRequests != null ? pullRequests : new PullRequest[0]);
    }
}
