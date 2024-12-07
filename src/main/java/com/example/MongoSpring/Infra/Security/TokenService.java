package com.example.MongoSpring.Infra.Security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.MongoSpring.Enity.users.Users;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Users user) {
        try {
            // Inicializa o algoritmo com a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Cria o token com informações do usuário
            return JWT.create()
                    .withIssuer("auth-api") // Emissor do token
                    .withSubject(user.getName()) // Sujeito do token (nome do usuário)
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm); // Assina o token
        } catch (JWTCreationException exception) {
            // Lida com exceções durante a criação do token
            throw new RuntimeException("Error  while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
