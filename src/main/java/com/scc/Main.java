package com.scc;

import com.scc.dao.UserDao;
import com.scc.model.User;
import com.scc.ui.AdminMenu;
import com.scc.ui.StudentMenu;
import com.scc.ui.TeacherMenu;

import java.util.Scanner;

public class Main {
    private static final UserDao userDao = new UserDao();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n==================================");
            System.out.println("   GRADE MANAGEMENT SYSTEM LOGIN  ");
            System.out.println("==================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> handleRegistration();
                case "3" -> {
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void handleLogin() {
        System.out.print("\nEnter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = userDao.login(username, password);
        if (user != null) {
            System.out.println("\n-> Welcome, " + user.username() + "! Role: [" + user.role() + "]");
            switch (user.role()) {
                case "ADMIN" -> new AdminMenu(userDao, scanner).display(user);
                case "TEACHER" -> new TeacherMenu(scanner).display(user);
                case "STUDENT" -> new StudentMenu(scanner).display(user);
                default -> System.out.println("Unknown role detected.");
            }
        }
    }

    private static void handleRegistration() {
        System.out.print("\nEnter new Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.println("Select Role:");
        System.out.println("1. Teacher");
        System.out.println("2. Student");
        System.out.print("Choice: ");
        String roleChoice = scanner.nextLine();

        String role = switch (roleChoice) {
            case "1" -> "TEACHER";
            case "2" -> "STUDENT";
            default -> null;
        };

        if (role != null) {
            userDao.registerUser(username, password, role);
        } else {
            System.out.println("Invalid role selected. Registration cancelled.");
        }
    }
}