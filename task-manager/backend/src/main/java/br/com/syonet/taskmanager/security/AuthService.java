package br.com.syonet.taskmanager.security;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.syonet.taskmanager.dto.LoginDTO;
import br.com.syonet.taskmanager.dto.TokenDTO;
import br.com.syonet.taskmanager.dto.UserDTO;
import br.com.syonet.taskmanager.entity.User;
import br.com.syonet.taskmanager.utils.JwtUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;


@ApplicationScoped
public class AuthService {

    @Inject
    JwtUtil jwtUtil;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    public String authenticate(String email, String password) {
        User user = User.findByEmail(email);
        if (user == null || !verifyPassword(password, user.senha)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return generateToken(user);
    }

    public TokenDTO login(LoginDTO loginDTO) {
    User user = User.findByEmail(loginDTO.email);
    if (user == null || !verifyPassword(loginDTO.senha, user.senha)) {
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

        return generateTokens(user.email, List.of(user.role.name()));
}


    @Transactional
    public TokenDTO register(UserDTO userDTO) {
        if (User.findByEmail(userDTO.email) != null) {
        throw new WebApplicationException("Email já cadastrado", Response.Status.CONFLICT);
    }

        User user = new User();
        user.email = userDTO.email;
        user.nome = userDTO.nome;
        user.role = User.UserRole.valueOf(userDTO.role);
        user.senha = hashPassword(userDTO.senha);
        user.persist();

        return generateTokens(user.email, List.of(user.role.name()));
}


    private String generateToken(User user) {
        return jwtUtil.generateToken(user.email, List.of(user.role.name()), 10); // 10 minutos
    }

    public TokenDTO generateTokens(String email, List<String> groups) {
    String accessToken = jwtUtil.generateToken(email, groups, 10); // 10 minutos
    String refreshToken = jwtUtil.generateToken(email, groups, 1440); // 1 dia

        return new TokenDTO(accessToken, refreshToken);
}


    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    private boolean verifyPassword(String password, String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
    }
}
