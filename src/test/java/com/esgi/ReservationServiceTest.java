package com.esgi;

import com.esgi.entities.Reservation;
import com.esgi.services.BookService;
import com.esgi.services.ReservationService;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ReservationServiceTest {
    ReservationService reservationService = new ReservationService("src/test/java/com/esgi/data/");
    BookService bookService = new BookService("src/test/java/com/esgi/data/");

    @Test
    public void saveReservationTest(){
        String title = "Earthsea";
        String author = "Ursula LeGain";
        bookService.tryToAddABook(title, author);
        boolean success = reservationService.saveReservation("1","john");
        Assert.assertTrue(success);
    }

    @Test
    public void getMyReservationsTest(){
        List<Reservation> reservationList = reservationService.getMyReservations("john");
        Assert.assertEquals(reservationList.size(), 1);
        Reservation reservation = reservationList.get(0);
        Assert.assertEquals(reservation.getBookId(), "1");
        Assert.assertEquals(reservation.getUserLogin(), "john");
    }

    @Test
    public void removeReservationTest(){
        boolean success = reservationService.removeReservation("1", "john");
        Assert.assertTrue(success);
    }

}
