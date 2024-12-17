package com.example.MongoSpring.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.MongoSpring.Enity.Product;

public interface ProductRepo extends MongoRepository<Product, Integer> {
    List<Product> findByCategory(String category);

    Optional<Product> findByName(String name);

    void deleteByName(String name);
}
