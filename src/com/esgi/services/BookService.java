package com.esgi.services;

import com.esgi.entities.Book;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class BookService {

    private static final JSONParser jsonParser = new JSONParser();
    private static final String filePath = "./src/com/esgi/data/books.json";

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
        try (FileReader reader = new FileReader("./src/com/esgi/data/books.json"))
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

                Book book = new Book((String) bookJson.get("id"),
                        (String) bookJson.get("title"),
                        (String) bookJson.get("author"),
                        (Boolean) bookJson.get("available"));
                newBooks.add(bookToSave);
                System.out.println(book.toString());
            }

            saveBooks(newBooks);

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
