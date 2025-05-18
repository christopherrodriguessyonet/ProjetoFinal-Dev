package br.com.syonet.taskmanager.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.syonet.taskmanager.entity.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.time.Duration;
import java.util.Set;
import br.com.syonet.taskmanager.dto.UserDTO;
import br.com.syonet.taskmanager.dto.LoginDTO;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthService {
    
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;
    
    public String authenticate(String email, String password) {
        User user = User.findByEmail(email);
        if (user == null || !verifyPassword(password, user.senha)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return generateToken(user);
    }

    public String login(LoginDTO loginDTO) {
        return authenticate(loginDTO.email, loginDTO.senha);
    }

    @Transactional
    public String register(UserDTO userDTO) {
        if (User.findByEmail(userDTO.email) != null) {
            throw new WebApplicationException("Email já cadastrado", Response.Status.CONFLICT);
        }
        User user = new User();
        user.email = userDTO.email;
        user.nome = userDTO.nome;
        user.role = User.UserRole.valueOf(userDTO.role);
        user.senha = hashPassword(userDTO.senha);
        user.persist();
        return generateToken(user);
    }
    
    private String generateToken(User user) {
        return Jwt.issuer(issuer)
            .audience("taskmanager") 
            .subject(user.email)
            .groups(Set.of(user.role.name()))
            .expiresIn(Duration.ofHours(24))
            .sign();
    }
    
    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    
    private boolean verifyPassword(String password, String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
    }
}
