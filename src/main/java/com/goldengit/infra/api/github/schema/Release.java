package com.goldengit.infra.api.github.schema;

public class Release {
    public long id;
    public String html_url;
    public GitUser author;
    public String name;
    public String tag_name;
    public String published_at;
    public String draft;
    public String target_commitish;
}
