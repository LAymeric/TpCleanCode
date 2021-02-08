package com.esgi.services;

import com.esgi.entities.Book;
import com.esgi.entities.ExtendedBook;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BookService {

    private static final JSONParser jsonParser = new JSONParser();
    private static final String filePath = "./src/com/esgi/data/books.json";

    public static void displayUserBooks(String userLogin){
        List<Reservation> reservations = ReservationService.getMyReservations(userLogin);
        List<ExtendedBook> myBooks = getBooksFromReservations(reservations);
        displayBooks(myBooks);
    }

    public static boolean tryToGiveBackABook(String bookId, String userId){
        boolean success = false;
        try (FileReader reader = new FileReader(filePath))
        {
            JSONArray newBooks = new JSONArray();
            //Read JSON file
            JSONArray books = (JSONArray) jsonParser.parse(reader);
            for (JSONObject bookJson : (Iterable<JSONObject>) books) {
                String bookJsonId = (String) bookJson.get("id");
                JSONObject bookToSave = new JSONObject();
                if(bookJsonId.equals(bookId)){
                    bookToSave.put("available", true);
                    success = true;
                }else{
                    bookToSave.put("available", bookJson.get("available"));
                }
                bookToSave.put("id", bookJson.get("id"));
                bookToSave.put("title", bookJson.get("title"));
                bookToSave.put("author", bookJson.get("author"));
                newBooks.add(bookToSave);
            }

            saveBooks(newBooks);
            ReservationService.removeReservation(bookId, userId);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return success;
    }

    private static List<ExtendedBook> getBooksFromReservations(List<Reservation> reservations){
        List<ExtendedBook> myBooks = new ArrayList<>();
        reservations.forEach(res -> {
            Book book = getBookById(res.getBookId());
            if(book != null){
                LocalDate maxGivenBackDate = LocalDate.parse(res.getDate());
                ExtendedBook extendedBook = new ExtendedBook(book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getAvailable(),
                        maxGivenBackDate.plusWeeks(4));
                myBooks.add(extendedBook);
            }
        });
        return myBooks;
    }

    private static Book getBookById(String idBook){
        Book book = null;
        try (FileReader reader = new FileReader(filePath))
        {
            //Read JSON file
            JSONArray books = (JSONArray) jsonParser.parse(reader);
            for (JSONObject bookJson : (Iterable<JSONObject>) books) {
                String id = (String) bookJson.get("id");
                if(idBook.equals(id)){
                    book = new Book(id,
                            (String) bookJson.get("title"),
                            (String) bookJson.get("author"),
                            (Boolean) bookJson.get("available"));
                    break;
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return book;
    }

    private static void displayBooks(List<ExtendedBook> books){
        if(books.size() > 0 ){
            books.forEach(book -> System.out.println(book.toString()));
        } else {
            System.out.println("You have no book yet.");
        }
    }

    public static void displayAllBooks(){
        try (FileReader reader = new FileReader(filePath))
        {
            //Read JSON file
            JSONArray books = (JSONArray) jsonParser.parse(reader);
            for (JSONObject bookJson : (Iterable<JSONObject>) books) {
                Book book = new Book((String) bookJson.get("id"),
                        (String) bookJson.get("title"),
                        (String) bookJson.get("author"),
                        (Boolean) bookJson.get("available"));
                System.out.println(book.toString());
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean tryToBorrowABook(String bookId, String userId){
        // first check if book exists, and if available
        boolean success = false;
        try (FileReader reader = new FileReader(filePath))
        {
            JSONArray newBooks = new JSONArray();
            //Read JSON file
            JSONArray books = (JSONArray) jsonParser.parse(reader);
            for (JSONObject bookJson : (Iterable<JSONObject>) books) {
                String bookJsonId = (String) bookJson.get("id");
                JSONObject bookToSave = new JSONObject();
                if(bookJsonId.equals(bookId)){
                    //the book exists
                    Book book = new Book(bookJsonId,
                            (String) bookJson.get("title"),
                            (String) bookJson.get("author"),
                            (Boolean) bookJson.get("available"));
                    if(book.getAvailable()){
                        bookToSave.put("available", false);
                        success = true;
                    }
                }else{
                    bookToSave.put("available", bookJson.get("available"));
                }
                bookToSave.put("id", bookJson.get("id"));
                bookToSave.put("title", bookJson.get("title"));
                bookToSave.put("author", bookJson.get("author"));
                newBooks.add(bookToSave);
            }

            saveBooks(newBooks);
            ReservationService.saveReservation(bookId, userId);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return success;
    }


    private static void saveBooks(JSONArray books){
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(books.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}