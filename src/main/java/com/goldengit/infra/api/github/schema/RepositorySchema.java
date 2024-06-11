package com.goldengit.infra.api.github.schema;

public class RepositorySchema {
    public int id;
    public OwnerSchema owner;
    public String name;
    public String full_name;
    public String description;
    public int stargazers_count;
    public int watchers_count;
    public int forks_count;
    public int open_issues_count;
    public String default_branch;
}