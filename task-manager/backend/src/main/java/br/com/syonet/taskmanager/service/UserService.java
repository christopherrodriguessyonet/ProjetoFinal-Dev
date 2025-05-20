package br.com.syonet.taskmanager.service;

import br.com.syonet.taskmanager.dto.UserDTO;
import br.com.syonet.taskmanager.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors; 
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class UserService {

    public List<UserDTO> listUsers(String usuario, String perfil) {
        List<User> users = User.listAll();

        // Filtro por nome ou e-mail (usuario)
        if (usuario != null && !usuario.isEmpty()) {
            users = users.stream()
                .filter(user -> user.nome.toLowerCase().contains(usuario.toLowerCase()) || 
                                user.email.toLowerCase().contains(usuario.toLowerCase()))
                .collect(Collectors.toList());
        }

        // Filtro por perfil (ADMIN ou USER)
        if (perfil != null && !perfil.isEmpty()) {
            users = users.stream()
                .filter(user -> user.role.name().equalsIgnoreCase(perfil))
                .collect(Collectors.toList());
        }

        // Converte a lista de usuários para DTO e retorna
        return users.stream()
            .map(UserDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", 404);
        }
        return UserDTO.fromEntity(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.nome = userDTO.nome;
        user.email = userDTO.email;
        user.role = User.UserRole.valueOf(userDTO.role);
        user.senha = userDTO.senha;
        user.persist();
        return UserDTO.fromEntity(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", 404);
        }
        user.nome = userDTO.nome;
        user.email = userDTO.email;
        user.role = User.UserRole.valueOf(userDTO.role);
        if (userDTO.senha != null && !userDTO.senha.isEmpty()) {
            user.senha = userDTO.senha;
        }
        return UserDTO.fromEntity(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", 404);
        }
        user.delete();
    }
}
