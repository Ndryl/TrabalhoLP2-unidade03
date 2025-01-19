package com.example.MongoSpring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

import com.example.MongoSpring.Enity.MyObject;
import com.example.MongoSpring.Enity.users.Users;
@AllArgsConstructor
@Data
public class UserDTO {
    private Users user;
    private List<MyObject> objetos;

}
