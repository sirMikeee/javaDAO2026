package com.scc.dao;

public class DatabaseInitializer {
    public static void initialize() {
        // Calling constructors executes their internal table creation logic
        new UserDao();
        new StudentDao();
        System.out.println("-> Database tables verified.");
    }
}