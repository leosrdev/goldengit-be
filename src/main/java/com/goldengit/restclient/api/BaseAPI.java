package com.goldengit.restclient.api;

import org.springframework.beans.factory.annotation.Value;

public abstract class BaseAPI {
    @Value("${github.api.token}")
    protected String apiToken;
}
