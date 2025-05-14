package br.com.syonet.taskmanager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    
    public enum UserRole {
        USER, ADMIN
    }
    @Column(unique = true)
    public String email;
    public String nome;
    public String senha;

    @Enumerated(EnumType.STRING)
    public UserRole role;
    public static User findByEmail(String email) {
        return find("email", email).firstResult();
    }
}