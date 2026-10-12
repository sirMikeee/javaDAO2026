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
            System.out.println("\n==========================================");
            System.out.println("            ADMIN DASHBOARD               ");
            System.out.println("==========================================");
            System.out.println("1. View All Users");
            System.out.println("2. View Pending User Approvals");
            System.out.println("3. Approve User Registration");
            System.out.println("4. Add New User (Manual)");
            System.out.println("5. Update User Role / Status");
            System.out.println("6. Delete User Account");
            System.out.println("7. Logout");
            System.out.print("Choose an option (1-7): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> viewAllUsers();
                case "2" -> viewPendingUsers();
                case "3" -> approveUser();
                case "4" -> addUserManual();
                case "5" -> updateUser(admin);
                case "6" -> deleteUser(admin);
                case "7" -> {
                    loggedIn = false;
                    System.out.println("-> Logging out from Admin Dashboard...");
                }
                default -> System.out.println("-> [ERROR] Invalid option. Please choose between 1 and 7.");
            }
        }
    }

    private void displayUserTable(List<User> users, String title) {
        System.out.println("\n================ " + title + " ================");
        if (users.isEmpty()) {
            System.out.println("-> No records found.");
            return;
        }

        System.out.println("+-----+------------------+------------+-----------+");
        System.out.printf("| %-3s | %-16s | %-10s | %-9s |\n", "ID", "Username", "Role", "Status");
        System.out.println("+-----+------------------+------------+-----------+");

        for (User u : users) {
            System.out.printf("| %-3d | %-16s | %-10s | %-9s |\n",
                    u.id(), u.username(), u.role(), u.status());
        }

        System.out.println("+-----+------------------+------------+-----------+");
    }

    private void viewAllUsers() {
        displayUserTable(userDao.getAllUsers(), "ALL REGISTERED USERS");
    }

    private void viewPendingUsers() {
        displayUserTable(userDao.getPendingUsers(), "PENDING APPROVALS");
    }

    private void approveUser() {
        List<User> pendingUsers = userDao.getPendingUsers();
        displayUserTable(pendingUsers, "PENDING APPROVALS");

        if (pendingUsers.isEmpty()) {
            return;
        }

        int userId = readIntInput("Enter User ID to approve (or 0 to cancel): ");
        if (userId == 0) {
            System.out.println("-> Operation cancelled.");
            return;
        }

        boolean exists = pendingUsers.stream().anyMatch(u -> u.id() == userId);
        if (!exists) {
            System.out.println("-> [ERROR] User ID not found in the pending list.");
            return;
        }

        userDao.approveUser(userId);
    }

    private void addUserManual() {
        System.out.println("\n--- ADD NEW USER ---");
        System.out.print("Enter Username (or 0 to cancel): ");
        String username = scanner.nextLine().trim();

        if (username.equals("0") || username.equalsIgnoreCase("CANCEL")) {
            System.out.println("-> Operation cancelled.");
            return;
        }

        if (username.isEmpty()) {
            System.out.println("-> [ERROR] Username cannot be empty.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("-> [ERROR] Password cannot be empty.");
            return;
        }

        String role = readValidRole();
        if (role == null) {
            System.out.println("-> Operation cancelled.");
            return;
        }

        userDao.createUserDirectly(username, password, role);
    }

    private void updateUser(User currentAdmin) {
        List<User> users = userDao.getAllUsers();
        displayUserTable(users, "SELECT USER TO UPDATE");

        if (users.isEmpty()) {
            return;
        }

        int userId = readIntInput("Enter User ID to update (or 0 to cancel): ");
        if (userId == 0) {
            System.out.println("-> Operation cancelled.");
            return;
        }

        if (userId == currentAdmin.id()) {
            System.out.println("-> [WARNING] Modifying your active Admin account role or status is restricted.");
            return;
        }

        String newRole = readValidRole();
        if (newRole == null) {
            System.out.println("-> Operation cancelled.");
            return;
        }

        String newStatus = readValidStatus();
        if (newStatus == null) {
            System.out.println("-> Operation cancelled.");
            return;
        }

        userDao.updateUserRoleAndStatus(userId, newRole, newStatus);
    }

    private void deleteUser(User currentAdmin) {
        List<User> users = userDao.getAllUsers();
        displayUserTable(users, "SELECT USER TO DELETE");

        if (users.isEmpty()) {
            return;
        }

        int userId = readIntInput("Enter User ID to delete (or 0 to cancel): ");
        if (userId == 0) {
            System.out.println("-> Operation cancelled.");
            return;
        }

        if (userId == currentAdmin.id()) {
            System.out.println("-> [ERROR] You cannot delete the currently logged-in Admin account!");
            return;
        }

        System.out.print("Are you sure you want to delete User ID " + userId + "? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            userDao.deleteUser(userId);
        } else {
            System.out.println("-> Deletion cancelled.");
        }
    }

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("-> [ERROR] Please enter a valid numeric ID.");
            }
        }
    }

    private String readValidRole() {
        while (true) {
            System.out.print("Enter Role (STUDENT / TEACHER / ADMIN) [0 to cancel]: ");
            String role = scanner.nextLine().trim().toUpperCase();
            if (role.equals("0") || role.equals("CANCEL")) {
                return null;
            }
            if (role.equals("STUDENT") || role.equals("TEACHER") || role.equals("ADMIN")) {
                return role;
            }
            System.out.println("-> [ERROR] Invalid role. Must be STUDENT, TEACHER, or ADMIN.");
        }
    }

    private String readValidStatus() {
        while (true) {
            System.out.print("Enter Status (APPROVED / PENDING) [0 to cancel]: ");
            String status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("0") || status.equals("CANCEL")) {
                return null;
            }
            if (status.equals("APPROVED") || status.equals("PENDING")) {
                return status;
            }
            System.out.println("-> [ERROR] Invalid status. Must be APPROVED or PENDING.");
        }
    }
}