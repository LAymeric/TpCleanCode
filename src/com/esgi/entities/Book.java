package com.esgi.entities;

public class Book {
    private int id;
    private String title;
    private String author;
    private Boolean borrowed;

    public Book(int id, String title, String author, Boolean borrowed) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.borrowed = borrowed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
    public Boolean getBorrowed() {
        return borrowed;
    }
}
