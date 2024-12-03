package com.example.MongoSpring.Enity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Products")
public class Product {

    @Id
    private String Id;
    private String name;
    private double price;
    private Integer quantity;
    private String description;

}
