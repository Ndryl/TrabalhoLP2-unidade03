package com.example.MongoSpring.Enity.users;

public enum UserRole {
    ADMIN("admin"),
    USER("users");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
