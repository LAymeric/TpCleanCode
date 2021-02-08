package com.esgi.entities;

import org.json.simple.JSONObject;

public class User {
    private UserType type;
    private String login;

    public User(UserType type, String login) {
        this.type = type;
        this.login = login;
    }

    public UserType getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type.toString());
        jsonObject.put("login", login);
        return jsonObject;
    }
}
