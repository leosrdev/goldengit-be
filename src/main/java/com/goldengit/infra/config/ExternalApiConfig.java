package com.goldengit.infra.config;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ExternalApiConfig {

    @Value("${github.api.token}")
    protected String apiToken;

    @Bean
    public GitHub gitHubApi() throws IOException {
        return new GitHubBuilder().withOAuthToken(apiToken).build();
    }
}
