package com.esgi;

import com.esgi.entities.User;
import com.esgi.entities.UserType;
import com.esgi.services.BookService;
import com.esgi.services.UserService;
import java.util.Scanner;

public class Main {

    private static User connectedUser;
    private static Scanner s;
    private static BookService bookService;
    private static UserService userService;

    public static void main(String[] args) {
        bookService = new BookService("src/main/java/com/esgi/data/");
        userService = new UserService("src/main/java/com/esgi/data/");
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
            if(connectedUser.getType().equals(UserType.LIBRARIAN)){
                System.out.println("3 - Add a new book");
            }
            System.out.println("4 - Logout");
        }

        manageMenuChoice(s.nextLine());
    }

    private static void manageMenuChoice(String input){
        switch(input){
            case "0":
                displayLogin();
                break;
            case "1":
                displayBrowseBooks();
                break;
            case "2":
                if(connectedUser.getType().equals(UserType.MEMBER)){
                    displayMyBooks();
                }else{
                    displayMenu();
                }
                break;
            case "3":
                if(connectedUser.getType().equals(UserType.LIBRARIAN)){
                    displayAddABook();
                }else{
                    displayMenu();
                }
                break;
            case "4":
                if(connectedUser != null){
                    displayLogout();
                }else{
                    displayMenu();
                }
                break;
            default:
                System.out.println("You can't choose that, please try again ! ");
                displayMenu();
                break;
        }
    }

    private static void displayAddABook(){
        System.out.println("You can add a book here. ");
        System.out.println("Please type the title :");
        String title = s.nextLine();
        System.out.println("Please type the author :");
        String author = s.nextLine();
        boolean success = bookService.tryToAddABook(title, author);
        if(success){
            System.out.println("Bravo ! You add a new book to the library");
        }else {
            System.out.println("Oh no! you can't add that book...");
        }
        displayMenu();
    }

    private static void displayMyBooks(){
        System.out.println("Here are your books. Please type the id of a book to give it back. Press q to go back to the menu");
        bookService.displayUserBooks(connectedUser.getLogin());
        String bookId = s.nextLine();
        if(!bookId.equals("q")){
            boolean success = bookService.tryToGiveBackABook(bookId, connectedUser.getLogin());
            if(success){
                System.out.println("You successfully gave back a book.");
            }else{
                System.out.println("Oh no, you can't give that back!");
            }
        }
        displayMenu();
    }

    private static void displayLogin(){
        System.out.println("You chose to login. Please enter your login or a new one to continue : ");
        String login = s.nextLine();
        connectedUser = userService.login(login);
        System.out.println("Wonderful ! you are now connected as " + connectedUser.getLogin());
        displayMenu();
    }

    private static void displayLogout(){
        System.out.println("You chose to logout. Have a nice day ! ");
        connectedUser = null;
        displayMenu();
    }

    private static void displayBrowseBooks(){
        System.out.println("\n\nThis is our book collection : ");
        bookService.displayAllBooks();
        if(connectedUser != null){
            System.out.println("If you would like to borrow a book, please type its id. If you want to go back to the menu, press q ");
        }
        System.out.println("If you want to go back to the menu, press q");
        String choice = s.nextLine();
        if(choice.equals("q")){
            displayMenu();
        }else if(connectedUser != null){
            boolean succeedToBorrowABook = bookService.tryToBorrowABook(choice, connectedUser.getLogin());
            if(succeedToBorrowABook){
                System.out.println("Bravo ! You just borrowed a book! you can retrieve it in the section 'My Books'. You have four weeks to bring it back");
            }else{
                System.out.println("Ooops ! you can't borrow that. Please check your input");
            }
            displayMenu();
        }
    }
}
