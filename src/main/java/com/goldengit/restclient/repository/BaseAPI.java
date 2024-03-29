package com.goldengit.restclient.repository;

import org.springframework.beans.factory.annotation.Value;

public abstract class BaseAPI {
    @Value("${github.api.token}")
    protected String apiToken;
}
