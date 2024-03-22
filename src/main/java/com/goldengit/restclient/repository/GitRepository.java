package com.goldengit.restclient.repository;

import com.goldengit.restclient.schema.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GitRepository {

    @Value("${github.api.token}")
    private String apiToken;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Repositories findRepoByQuery(String query) {
        return webClientBuilder.build()
                .get()
                .uri("https://api.github.com/search/repositories?q=" + query)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(apiToken))
                .retrieve()
                .bodyToMono(Repositories.class)
                .block();
    }
}
