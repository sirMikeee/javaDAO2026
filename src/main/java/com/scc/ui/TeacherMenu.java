package com.scc.ui;

import com.scc.model.User;

import java.util.Scanner;

public class TeacherMenu {
    private final Scanner scanner;

    public TeacherMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void display(User teacher) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- TEACHER DASHBOARD ---");
            System.out.println("1. View My Profile");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> System.out.println("Logged in as: " + teacher);
                case "2" -> {
                    loggedIn = false;
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}