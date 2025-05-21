package br.com.syonet.taskmanager.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.com.syonet.taskmanager.dto.TokenPayload;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JwtUtil {

    @Inject
    JWTParser jwtParser;

    public TokenPayload parse(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            JsonWebToken parsedJwt = jwtParser.parse(token);

            TokenPayload payload = new TokenPayload();
            payload.sub = parsedJwt.getSubject();
            payload.groups = parsedJwt.getGroups().stream().toList();
            payload.exp = parsedJwt.getExpirationTime();
            return payload;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao parsear token. Causa: " + e.getMessage(), e);
        }
    }

    public String generateToken(String email, List<String> groups, int minutes) {
        return Jwt.issuer("http://localhost:8080")
                .subject(email)
                .groups(Set.copyOf(groups))
                .issuedAt(Instant.now())
                .expiresIn(Duration.ofMinutes(minutes))
                .claim("aud", "taskmanager")
                .sign();
    }

}