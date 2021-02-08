package com.esgi.entities;

import org.json.simple.JSONObject;

public class Reservation {
    private String userId;
    private String bookId;
    private String date;

    public Reservation(String userId, String bookId, String date) {
        this.userId = userId;
        this.bookId = bookId;
        this.date = date;
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("bookId", bookId);
        jsonObject.put("date", date);
        return jsonObject;
    }
}
