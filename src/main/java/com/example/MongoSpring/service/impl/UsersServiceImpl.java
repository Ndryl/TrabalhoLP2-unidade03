package com.example.MongoSpring.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MongoSpring.DTO.UserDTO;
import com.example.MongoSpring.Enity.MyObject;
import com.example.MongoSpring.Enity.users.Users;
import com.example.MongoSpring.Repository.UserRepo;
import com.example.MongoSpring.Repository.ObjectRepo; // Importar o repositório de objetos
import com.example.MongoSpring.service.UserService;

@Service
public class UsersServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectRepo objectRepo; // Injetar o repositório de objetos

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

    @Override
    public Optional<UserDTO> getUsuarioComObjetos(String userId) {
        // Buscar usuário
        Optional<Users> userOpt = userRepo.findById(userId); // Corrigido para usar userRepo
        if (userOpt.isPresent()) {
            // Buscar objetos relacionados ao usuário
            List<MyObject> objetos = objectRepo.findByUserId(userId); // Usar objectRepo corretamente

            // Retornar DTO com informações do usuário e objetos
            Users user = userOpt.get();
            return Optional.of(new UserDTO(user, objetos));
        }
        return Optional.empty();
    }
}
