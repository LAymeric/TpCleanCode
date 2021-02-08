package com.esgi.services;

import com.esgi.entities.Reservation;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ReservationService {
    private static final String filePath = "./src/com/esgi/data/reservations.json";

    public static void saveReservation(String bookId, String userId){
        Reservation newReservation = new Reservation(userId, bookId, new Date().toString());
        JSONObject reservationJson = newReservation.toJSONObject();
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(reservationJson.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
