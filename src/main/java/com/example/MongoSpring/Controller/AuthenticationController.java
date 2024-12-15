package com.example.MongoSpring.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import com.example.MongoSpring.Enity.users.AutheticationDTO;
import com.example.MongoSpring.Enity.users.RegisterDTO;
import com.example.MongoSpring.Enity.users.UserResponseDTO;
import com.example.MongoSpring.Enity.users.Users;
import com.example.MongoSpring.Infra.Security.TokenService;
import com.example.MongoSpring.Repository.UserRepo;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AutheticationDTO data, HttpServletResponse response) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(data.name(), data.password());
        var auth = this.authenticationManager.authenticate(userNamePassword);

        // Gera o token JWT
        var token = tokenService.generateToken((Users) auth.getPrincipal());

        // Configura o cookie
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true); // Impede acesso via JavaScript
        cookie.setSecure(false); // Use true em produção com HTTPS
        cookie.setPath("/"); // Disponível em todas as rotas do domínio
        cookie.setMaxAge(60 * 60); // Expiração em segundos (1 hora, por exemplo)

        // Adiciona o cookie à resposta
        response.addCookie(cookie);

        // Retorna uma resposta de sucesso (opcionalmente pode retornar mais informações
        // no corpo)
        return ResponseEntity.ok(new UserResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity registre(@RequestBody @Valid RegisterDTO data) {
        if (this.repository.findByname(data.name()) != null) {
            return ResponseEntity.badRequest().build();
        } else {
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            Users newUsers = new Users(data.name(), encryptedPassword, data.role());
            this.repository.save(newUsers);
            return ResponseEntity.ok().build();
        }
    }
}
