package com.example.MongoSpring.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MongoSpring.Enity.users.Users;
import com.example.MongoSpring.Repository.UserRepo;
import com.example.MongoSpring.service.UserService;

@Service
public class UsersServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<Users> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public Users addUsers(Users user) {
        return userRepo.save(user);
    }

    @Override
    public Users deleteUser(String id) {
        Users user = userRepo.findById(id).get();
        userRepo.delete(user);
        return user;
    }

    @Override
    public Users updateUser(String id, Users user) {
        Users userVar = userRepo.findById(id).get();
        userVar.setName(user.getName());
        userVar.setEmail(user.getEmail());
        userVar.setPassword(user.getPassword());
        userRepo.save(userVar);

        return userVar;
    }

}
