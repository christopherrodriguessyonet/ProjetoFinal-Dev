package br.com.syonet.taskmanager.resource;

import br.com.syonet.taskmanager.dto.LoginDTO;
import br.com.syonet.taskmanager.dto.RefreshTokenDTO;
import br.com.syonet.taskmanager.dto.TokenDTO;
import br.com.syonet.taskmanager.dto.UserDTO;
import br.com.syonet.taskmanager.security.AuthService;
import br.com.syonet.taskmanager.utils.JwtUtil;
import br.com.syonet.taskmanager.dto.TokenPayload;
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

    @Inject
    JwtUtil jwtUtil;

    @POST
    @Path("/login")
    public Response login(@Valid LoginDTO loginDTO) {
        TokenDTO tokenDTO = authService.login(loginDTO);
        return Response.ok(tokenDTO).build();
}


    @POST
    @Path("/register")
    public Response register(@Valid UserDTO userDTO) {
        TokenDTO tokenDTO = authService.register(userDTO);
        return Response.ok(tokenDTO).build();
}


    @POST
    @Path("/hash")
    public String gerarHash(LoginDTO dto) {
        return authService.hashPassword(dto.senha);
    }

    @POST
    @Path("/refresh")
    public Response refreshToken(RefreshTokenDTO refreshDTO) {

        try {
            TokenPayload payload = jwtUtil.parse(refreshDTO.refreshToken);
            if (payload == null) {
                return Response.status(401).entity("Refresh token inválido").build();
            }

            TokenDTO newTokens = authService.generateTokens(payload.sub, payload.groups);
            return Response.ok(newTokens).build();
        } catch (Exception e) {
            return Response.status(401).entity("Erro ao renovar token").build();
        }
    }
}
