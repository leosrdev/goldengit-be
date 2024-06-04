package com.goldengit.restclient.schema;

public class PullRequest {
    public long id;
    public int number;
    public String title;
    public String state;
    public String body;
    public String created_at;
    public String closed_at;
    public GitUser user;
}
