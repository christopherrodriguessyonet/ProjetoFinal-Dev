package br.com.syonet.taskmanager.resource;

import br.com.syonet.taskmanager.dto.LoginDTO;
import br.com.syonet.taskmanager.dto.UserDTO;
import br.com.syonet.taskmanager.security.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import java.util.Map;


@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDTO) {
        try {
            String token = authService.authenticate(loginDTO.email, loginDTO.password);
            return Response.ok(new TokenDTO(token)).build();
        } catch (WebApplicationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/register")
    @Transactional
    public Response register(UserDTO userDTO) {
        try {
            String token = authService.register(userDTO);
            return Response.ok(new TokenDTO(token)).build();
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new WebApplicationException("Erro ao registrar usuário: " + e.getMessage(), 400);
        }
    }

    @POST
    @Path("/hash")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String hashPassword(java.util.Map<String, String> body) {
        return authService.hashPassword(body.get("password"));
    }

    public static class TokenDTO {
        public String token;

        public TokenDTO(String token) {
            this.token = token;
        }
    }
}