package com.scc.model;

public record Student(int id, String name, String email) {
    @Override
    public String toString() {
        return String.format("[%d] %s (%s)", id, name, email);
    }
}