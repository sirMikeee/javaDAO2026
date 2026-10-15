package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Authentication {
    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n==================================");
            System.out.println("   GRADE MANAGEMENT SYSTEM LOGIN  ");
            System.out.println("==================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> handleRegistration();
                case "3" -> {
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                }
                default -> System.out.println("-> [ERROR] Invalid option. Please try again.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("\nEnter Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        User user = userDao.login(username, password);
        if (user != null) {
            System.out.println("\n-> Welcome, " + user.username() + "! Role: [" + user.role() + "]");
            switch (user.role().toUpperCase()) {
                case "ADMIN" -> new AdminMenu(userDao, scanner).display(user);
                case "TEACHER" -> new TeacherMenu(scanner).display(user);
                case "STUDENT" -> new StudentMenu(scanner).display(user);
                default -> System.out.println("-> [ERROR] Unknown role detected.");
            }
        }
    }

    private void handleRegistration() {
        System.out.print("\nEnter new Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("-> [ERROR] Username and password cannot be empty.");
            return;
        }

        System.out.println("Select Role:");
        System.out.println("1. Teacher");
        System.out.println("2. Student");
        System.out.print("Choice: ");
        String roleChoice = scanner.nextLine().trim();

        String role = switch (roleChoice) {
            case "1" -> "TEACHER";
            case "2" -> "STUDENT";
            default -> null;
        };

        if (role != null) {
            userDao.registerUser(username, password, role);
        } else {
            System.out.println("-> [ERROR] Invalid role selected. Registration cancelled.");
        }
    }
}