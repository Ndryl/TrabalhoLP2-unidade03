package com.example.MongoSpring.service;


import com.example.MongoSpring.Enity.MyObject;
import com.example.MongoSpring.Repository.ObjectRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObjectService {

    @Autowired
    private ObjectRepo objectRepo;

    // Método para buscar todos os objetos
    public List<MyObject> getAllObjects() {
        return objectRepo.findAll();
    }

    // Método para buscar um objeto por ID
    public Optional<MyObject> getObjectById(String id) {
        return objectRepo.findById(id);
    }

    // Método para buscar objetos pelo ID do usuário
    public List<MyObject> getObjectsByUser(String user) {
        return objectRepo.findByUserId(user);
    }

    // Método para salvar um novo objeto
    public MyObject saveObject(MyObject myObject) {
        return objectRepo.save(myObject);
    }

    // Método para atualizar um objeto existente
    public MyObject updateObject(String id, MyObject updatedObject) {
        Optional<MyObject> optionalObject = objectRepo.findById(id);
        if (optionalObject.isPresent()) {
            MyObject existingObject = optionalObject.get();
            existingObject.setDate(updatedObject.getDate());
            existingObject.setPhoto(updatedObject.getPhoto());
            existingObject.setId(updatedObject.getId());
            return objectRepo.save(existingObject);
        } else {
            throw new RuntimeException("Objeto não encontrado com o ID: " + id);
        }
    }

    // Método para deletar um objeto pelo ID
    public void deleteObject(String id) {
        objectRepo.deleteById(id);
    }
}
