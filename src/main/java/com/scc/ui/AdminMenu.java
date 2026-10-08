package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final Scanner scanner;

    public AdminMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void display(User admin) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- ADMIN DASHBOARD ---");
            System.out.println("1. View Pending User Approvals");
            System.out.println("2. Approve User Registration");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> viewPendingUsers();
                case "2" -> approveUser();
                case "3" -> {
                    loggedIn = false;
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void viewPendingUsers() {
        List<User> pending = userDao.getPendingUsers();
        if (pending.isEmpty()) {
            System.out.println("-> No pending registrations found.");
        } else {
            System.out.println("\n================ PENDING APPROVALS ================");
            System.out.println("+-----+------------------+------------+-----------+");
            System.out.printf("| %-3s | %-16s | %-10s | %-9s |\n", "ID", "Username", "Role", "Status");
            System.out.println("+-----+------------------+------------+-----------+");

            for (User u : pending) {
                System.out.printf("| %-3d | %-16s | %-10s | %-9s |\n",
                        u.id(), u.username(), u.role(), u.status());
            }

            System.out.println("+-----+------------------+------------+-----------+");
        }
    }

    private void approveUser() {
        System.out.print("Enter User ID to approve: ");
        try {
            int userId = Integer.parseInt(scanner.nextLine());
            userDao.approveUser(userId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }
}