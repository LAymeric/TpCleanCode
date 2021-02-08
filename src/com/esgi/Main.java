package com.esgi;

import com.esgi.entities.User;
import com.esgi.entities.UserType;
import com.esgi.services.BookService;

import java.util.Scanner;

public class Main {

    private static User connectedUser;
    private static Scanner s;

    public static void main(String[] args) {
        System.out.println("Welcome to our library! ");
        s = new Scanner(System.in);
        displayMenu();
    }

    private static void displayMenu(){
        System.out.println("Please choose one of the following options: ");
        // if not connected
        if(connectedUser == null){
            System.out.println("0 - Login or Register ");
        }

        System.out.println("1 - Browse books");

        // is user is member
        if(connectedUser != null){
            if(connectedUser.getType().equals(UserType.MEMBER)){
                System.out.println("2 - My Books");
            }
            if(connectedUser.getType().equals(UserType.MEMBER)){
                System.out.println("3 - Add a new book");
            }
            System.out.println("4 - Logout");
        }

        manageMenuChoice(s.next());
    }

    private static void manageMenuChoice(String input){
        switch(input){
            case "0":
                break;
            case "1":
                displayBrowseBooks();
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            default:
                System.out.println("You can't choose that, please try again ! ");
                displayMenu();
                break;
        }
    }

    private static void displayBrowseBooks(){
        System.out.println("\n\nThis is our book collection : ");
        BookService.displayAllBooks();
        if(connectedUser != null){
            System.out.println("If you would like to borrow a book, please type its id. If you want to go back to the menu, press q ");
        }
        System.out.println(" If you want to go back to the menu, press q");
        String choice = s.next();
        if(choice.equals("q")){
            displayMenu();
        }else if(connectedUser != null){
            boolean succeedToBorrowABook = BookService.tryToBorrowABook(choice, connectedUser.getId());
            if(succeedToBorrowABook){
                System.out.println("Bravo ! You just borrowed a book! you can retrieve it in the section 'My Books'. You have four weeks to bring it back");
            }else{
                System.out.println("Ooops ! you can't borrow that. Please check your input");
            }
            displayMenu();
        }
    }
}
