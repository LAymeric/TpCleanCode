package com.esgi.entities;

public class User {
    private String id;
    private UserType type;
    private String login;

    public User(String id, UserType type, String login) {
        this.id = id;
        this.type = type;
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public UserType getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }
}
