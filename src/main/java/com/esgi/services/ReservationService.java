package com.esgi.services;

import com.esgi.entities.Reservation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private static final JSONParser jsonParser = new JSONParser();
    private static final String filePath = "./src/main/java/com/esgi/data/reservations.json";

    public static List<Reservation> getMyReservations(String userLogin){
        List<Reservation> reservationList = new ArrayList<>();
        JSONArray reservationsJson = getAllReservations();
        for (JSONObject resJson : (Iterable<JSONObject>) reservationsJson){
            reservationList.add(new Reservation(userLogin,
                    (String)resJson.get("bookId"),
                    (String)resJson.get("date")));
        }
        return reservationList;
    }

    public static void saveReservation(String bookId, String userLogin){
        Reservation newReservation = new Reservation(userLogin, bookId, LocalDate.now().toString());
        JSONArray allReservations = getAllReservations();
        JSONObject reservationJson = newReservation.toJSONObject();
        allReservations.add(reservationJson);
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(allReservations.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeReservation(String bookId, String userLogin){
        Reservation res = new Reservation(userLogin, bookId, LocalDate.now().toString());
        JSONArray allReservations = getAllReservations();
        JSONObject reservationJson = res.toJSONObject();
        allReservations.remove(reservationJson);
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(allReservations.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray getAllReservations(){
        JSONArray reservations = new JSONArray();
        try (FileReader reader = new FileReader(filePath))
        {
            reservations = (JSONArray) jsonParser.parse(reader);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}
