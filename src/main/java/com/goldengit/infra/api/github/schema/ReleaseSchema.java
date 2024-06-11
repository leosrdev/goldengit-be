package com.goldengit.infra.api.github.schema;

public class ReleaseSchema {
    public long id;
    public String html_url;
    public UserSchema author;
    public String name;
    public String tag_name;
    public String published_at;
    public String draft;
    public String target_commitish;
}
