package com.cranfield.search.model;

public class QueryModel {
    private final int id;       
    private final String text;

    public QueryModel(int id, String text) {
        this.id = id;
        this.text = text;
    }
    
    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
