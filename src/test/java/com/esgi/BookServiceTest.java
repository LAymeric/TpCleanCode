package com.esgi;

import com.esgi.services.BookService;
import org.json.simple.JSONArray;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

public class BookServiceTest {
    BookService bookService = new BookService("src/test/java/com/esgi/data/");

    @Test
    public void tryToAddABookTest(){
        String title = "Earthsea";
        String author = "Ursula LeGain";
        boolean success = bookService.tryToAddABook(title, author);
        Assert.assertTrue(success);
    }

    @Test
    public void tryToBorrowABook(){
        boolean failure = bookService.tryToBorrowABook("-3", "unknown");
        Assert.assertFalse(failure);
        boolean success = bookService.tryToBorrowABook("1", "john");
        Assert.assertTrue(success);
    }

    @Test
    public void tryToGiveBackABook(){
        boolean failure = bookService.tryToGiveBackABook("-3", "unknown");
        Assert.assertFalse(failure);
        boolean success = bookService.tryToGiveBackABook("1", "john");
        Assert.assertTrue(success);
    }

    @AfterClass
    public static void clearAll(){
        clear("books.json");
        clear("reservations.json");
    }

    public static void clear(String path){
        try (FileWriter file = new FileWriter("src/test/java/com/esgi/data/" + path)) {
            file.write(new JSONArray().toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
