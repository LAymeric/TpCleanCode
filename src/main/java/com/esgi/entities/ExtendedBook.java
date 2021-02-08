package com.esgi.entities;

import java.time.LocalDate;
import java.util.Date;

public class ExtendedBook extends Book{
    private LocalDate maxGivenBackDate;

    public ExtendedBook(String id, String title, String author, Boolean available, LocalDate maxGivenBackDate) {
        super(id, title, author, available);
        this.maxGivenBackDate = maxGivenBackDate;
    }

    @Override
    public String toString() {
        String result = "This book has to be given back before the : " + maxGivenBackDate + "\n";
        LocalDate now = LocalDate.now();
        if(now.isAfter(maxGivenBackDate)) {
            result += "Oh no ! YOU ARE LATE TO GIVE THIS BOOK BACK ! \n";
        }
        result += super.toString();
        return result;
    }
}
