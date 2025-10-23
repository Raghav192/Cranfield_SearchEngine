package com.cranfield.search.model;

public class CranDocModel {
    private final String id;
    private final String content;

    public CranDocModel(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}