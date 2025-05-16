package br.com.syonet.taskmanager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "users")
public class User extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

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