package com.example.MongoSpring.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.MongoSpring.Enity.users.Users;

public interface UserRepo extends MongoRepository<Users, String> {
    UserDetails findByname(String name);
}
