package com.esgi.entities;

public class User {
    private int id;
    private UserType type;
    private String login;

    public User(int id, UserType type, String login) {
        this.id = id;
        this.type = type;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public UserType getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }
}
