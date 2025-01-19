package com.example.MongoSpring.Enity.users;

public record RegisterDTO(String name, String password, String email,UserRole role) {

}
