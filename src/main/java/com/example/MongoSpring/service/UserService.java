package com.example.MongoSpring.service;

import java.util.List;

import com.example.MongoSpring.Enity.users.Users;

public interface UserService {

    public List<Users> getUsers();

    public Users addUsers(Users user);

    public Users deleteUser(String id);

    public Users updateUser(String id, Users user);

}
