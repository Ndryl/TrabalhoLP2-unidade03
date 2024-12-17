package com.example.MongoSpring.Enity;

import lombok.Data;

@Data
public class QuantityUpdateRequest {
    private String name;
    private int quantity;
}
