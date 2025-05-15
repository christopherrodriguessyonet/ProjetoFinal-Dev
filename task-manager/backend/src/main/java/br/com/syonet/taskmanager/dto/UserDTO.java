package br.com.syonet.taskmanager.dto;

import br.com.syonet.taskmanager.entity.User;
import br.com.syonet.taskmanager.entity.User.UserRole;

public class UserDTO {
    public Long id;
    public String email;
    public String nome;
    public UserRole role;
    public String senha; // usado apenas para criação/atualização
    
    public UserDTO() {}
    
    public UserDTO(Long id, String email, String nome, UserRole role) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.role = role;
    }
    
    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.id, user.email, user.nome, user.role);
    }
}