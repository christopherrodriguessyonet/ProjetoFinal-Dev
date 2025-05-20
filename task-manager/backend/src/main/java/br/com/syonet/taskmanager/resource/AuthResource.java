package br.com.syonet.taskmanager.resource;

import br.com.syonet.taskmanager.dto.LoginDTO;
import br.com.syonet.taskmanager.dto.UserDTO;
import br.com.syonet.taskmanager.security.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(@Valid LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        return Response.ok().entity(token).build();
    }

    @POST
    @Path("/register")
    public Response register(@Valid UserDTO userDTO) {
        String token = authService.register(userDTO);
        return Response.ok().entity(token).build();
    }

    @POST
    @Path("/hash")
    public String gerarHash(LoginDTO dto) {
        return authService.hashPassword(dto.senha);
    }
} 
