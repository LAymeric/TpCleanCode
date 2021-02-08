package com.esgi.entities;

import org.json.simple.JSONObject;

public class Reservation {
    private String userLogin;
    private String bookId;
    private String date;

    public Reservation(String userLogin, String bookId, String date) {
        this.userLogin = userLogin;
        this.bookId = bookId;
        this.date = date;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getBookId() {
        return bookId;
    }

    public String getDate() {
        return date;
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userLogin", userLogin);
        jsonObject.put("bookId", bookId);
        jsonObject.put("date", date);
        return jsonObject;
    }
}
