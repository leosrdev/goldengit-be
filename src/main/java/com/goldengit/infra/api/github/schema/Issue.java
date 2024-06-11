package com.goldengit.infra.api.github.schema;

public class Issue {
    public long id;
    public int number;
    public String title;
    public String state;
    public String body;
    public String created_at;
    public String closed_at;
}
