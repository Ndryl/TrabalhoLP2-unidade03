package com.example.MongoSpring.service;

import java.util.List;

import com.example.MongoSpring.Enity.Product;

public interface ProductService {

    public List<Product> getProducts();

    List<Product> getProduct(String category);

    public Product addProoducts(Product product);

    public Product deleProduct(int id);

    public Product updateProduct(int id, Product product);

}
