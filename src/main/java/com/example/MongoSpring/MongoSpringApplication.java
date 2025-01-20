package com.example.MongoSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import nu.pattern.OpenCV;

@SpringBootApplication
public class MongoSpringApplication {

	public static void main(String[] args) {
		// Inicializando o OpenCV
        OpenCV.loadShared();
		// Iniciando a aplicação Spring Boot
		SpringApplication.run(MongoSpringApplication.class, args);
        
        
	}

}
