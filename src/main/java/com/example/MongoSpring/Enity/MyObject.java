package com.example.MongoSpring.Enity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Objects") // Define a coleção no MongoDB
public class MyObject { // Nome alterado para evitar conflitos com java.lang.Object
    @Id
    private String id; // Nome do campo corrigido para seguir convenções de Java
    private LocalDate date; // Usando LocalDate para datas
    private byte[] photo;
    private String userId; // ID do usuário relacionado
}
