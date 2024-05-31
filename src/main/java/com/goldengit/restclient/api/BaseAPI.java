package com.goldengit.restclient.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

public abstract class BaseAPI {
    @Value("${github.api.token}")
    protected String apiToken;
    protected final static MediaType APPLICATION_JSON_GITHUB = MediaType.valueOf("application/vnd.github+json");
}
