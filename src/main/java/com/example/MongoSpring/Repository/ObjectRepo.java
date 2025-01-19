package com.example.MongoSpring.Repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.MongoSpring.Enity.MyObject;


public interface ObjectRepo extends MongoRepository<MyObject, String> {
    List<MyObject> findByUserId(String userId); // Método para buscar objetos pelo ID do usuário
}
