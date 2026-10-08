package com.scc.ui;

import com.scc.model.User;

import java.util.Scanner;

public class StudentMenu {
    private final Scanner scanner;

    public StudentMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void display(User student) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- STUDENT DASHBOARD ---");
            System.out.println("1. View My Profile");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> System.out.println("Logged in as: " + student);
                case "2" -> {
                    loggedIn = false;
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}