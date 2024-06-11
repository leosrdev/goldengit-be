package com.goldengit.infra.api.github.schema;

public class PullRequestSchema {
    public long id;
    public int number;
    public String html_url;
    public String title;
    public String state;
    public String body;
    public String created_at;
    public String closed_at;
    public UserSchema user;
}
