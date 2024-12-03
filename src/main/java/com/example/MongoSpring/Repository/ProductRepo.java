package com.example.MongoSpring.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.MongoSpring.Enity.Product;

public interface ProductRepo extends MongoRepository<Product, Integer> {

}
