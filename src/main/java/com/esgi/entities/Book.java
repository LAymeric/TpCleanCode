package com.esgi.entities;

import org.json.simple.JSONObject;

public class Book {
    private String id;
    private String title;
    private String author;
    private Boolean available;

    public Book(String id, String title, String author, Boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
    public Boolean getAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "Id : " + id
                + "\nTitle : " + title
                + "\nAuthor : " + author
                + "\nAvailability : " + (available ? "Yes" : "No")
                + "\n    ******";
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("title", title);
        jsonObject.put("author", author);
        jsonObject.put("available", available);
        return jsonObject;
    }
}
