package com.example.MongoSpring.Enity.users;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Users")
public class Users implements UserDetails {
    @Id
    private String id; // MongoDB gerencia o ID automaticamente
    private String name;
    private String email;
    private String password;
    private UserRole role; // Enum para representar os papéis de usuário

    public Users(String name, String password, String email,  UserRole userrole) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = userrole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorne as permissões do usuário com base no papel (role)
        if (this.role == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Retorne `true` se a conta não estiver expirada
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Retorne `true` se a conta não estiver bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Retorne `true` se as credenciais não estiverem expiradas
    }

    @Override
    public boolean isEnabled() {
        return true; // Retorne `true` se a conta estiver habilitada
    }

    @Override
    public String getUsername() {
        return getEmail(); // Use o campo `email` como nome de usuário
    }
}
