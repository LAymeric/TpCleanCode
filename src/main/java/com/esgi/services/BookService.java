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
import java.util.List;

public class BookService {

    private static final JSONParser jsonParser = new JSONParser();
    private final String filePath;
    private final ReservationService reservationService;

    public BookService(String filePath) {
        this.filePath = filePath + "books.json";
        this.reservationService = new ReservationService(filePath);
    }

    public void displayUserBooks(String userLogin){
        List<Reservation> reservations = this.reservationService.getMyReservations(userLogin);
        List<ExtendedBook> myBooks = getBooksFromReservations(reservations);
        displayBooks(myBooks);
    }

    public boolean tryToAddABook(String title, String author){
        boolean success = false;
        try (FileReader reader = new FileReader(this.filePath))
        {
            JSONArray books = (JSONArray) jsonParser.parse(reader);
            int nextIndex = books.size() + 1;
            Book newBook = new Book(String.valueOf(nextIndex), title, author, true);
            books.add(newBook.toJSONObject());
            saveBooks(books);
            success = true;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean tryToGiveBackABook(String bookId, String userId){
        boolean success = false;
        try (FileReader reader = new FileReader(this.filePath))
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
            if(success){
                success = this.reservationService.removeReservation(bookId, userId);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return success;
    }

    private List<ExtendedBook> getBooksFromReservations(List<Reservation> reservations){
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

    private Book getBookById(String idBook){
        Book book = null;
        try (FileReader reader = new FileReader(this.filePath))
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

    public void displayAllBooks(){
        try (FileReader reader = new FileReader(this.filePath))
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

    public boolean tryToBorrowABook(String bookId, String userId){
        // first check if book exists, and if available
        boolean success = false;
        try (FileReader reader = new FileReader(this.filePath))
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
            if(success){
                success = this.reservationService.saveReservation(bookId, userId);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return success;
    }

    private void saveBooks(JSONArray books){
        try (FileWriter file = new FileWriter(this.filePath)) {
            file.write(books.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
