package br.com.syonet.taskmanager.service;

import br.com.syonet.taskmanager.dto.UserDTO;
import br.com.syonet.taskmanager.entity.User;
import br.com.syonet.taskmanager.security.AuthService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    @Inject
    AuthService authService;

    public List<UserDTO> listUsers() {
        return User.<User>listAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return UserDTO.fromEntity(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (User.findByEmail(userDTO.email) != null) {
            throw new WebApplicationException("Email já cadastrado", Response.Status.CONFLICT);
        }

        User user = new User();
        user.email = userDTO.email;
        user.nome = userDTO.nome;
        user.role = User.UserRole.valueOf(userDTO.role);
        user.senha = authService.hashPassword(userDTO.senha);
        user.persist();
        return UserDTO.fromEntity(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        User existingEmail = User.findByEmail(userDTO.email);
        if (existingEmail != null && !existingEmail.id.equals(id)) {
            throw new WebApplicationException("Email já cadastrado", Response.Status.CONFLICT);
        }

        user.email = userDTO.email;
        user.nome = userDTO.nome;
        user.role = User.UserRole.valueOf(userDTO.role);
        if (userDTO.senha != null && !userDTO.senha.isEmpty()) {
            user.senha = authService.hashPassword(userDTO.senha);
        }

        return UserDTO.fromEntity(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        user.delete();
    }
} 