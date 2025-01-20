package com.example.MongoSpring.service;

import com.example.MongoSpring.DTO.UserDTO;
import com.example.MongoSpring.Enity.users.Users;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getUsuarioComObjetos(String userId); // Busca usuário e objetos relacionados
    List<Users> getUsers(); // Busca todos os usuários
    Users addUsers(Users user); // Adiciona um novo usuário
    Users deleteUser(String id); // Remove um usuário
    Users updateUser(String id, Users user); // Atualiza um usuário
    Users getUser(String id);
}
