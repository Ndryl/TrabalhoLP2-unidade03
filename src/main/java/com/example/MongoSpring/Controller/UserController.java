package com.example.MongoSpring.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MongoSpring.Enity.users.Users;
import com.example.MongoSpring.service.UserService;

@RestController
@RequestMapping("/api/Users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<Users> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/insert")
    public Users insert(@RequestBody Users user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser vazia!");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário não pode ser vazio!");
        }

        return userService.addUsers(user);

    }

    @PutMapping("/update")
    public Users update(@PathVariable String id, @RequestBody Users user) {
        return userService.updateUser(id, user);

    }

    @DeleteMapping("/delete/{id}")
    public Users delete(@PathVariable String id, @RequestBody Users user) {
        return userService.deleteUser(id);
    }

}
