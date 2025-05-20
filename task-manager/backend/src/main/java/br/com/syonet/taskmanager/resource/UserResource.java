package br.com.syonet.taskmanager.resource;

import br.com.syonet.taskmanager.dto.UserDTO;
import br.com.syonet.taskmanager.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import java.util.List;

@RolesAllowed("ADMIN")
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public List<UserDTO> listUsers(
        @QueryParam("usuario") String usuario, 
        @QueryParam("perfil") String perfil
    ) {
        List<UserDTO> users = userService.listUsers(usuario, perfil);
        if (users == null || users.isEmpty()) {
            throw new WebApplicationException("Nenhum usuário encontrado", 404);
        }
        return users;
    }

    @GET
    @Path("/{id}")
    public UserDTO getUser(@PathParam("id") Long id) {
        UserDTO user = userService.getUser(id);
        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", 404);
        }
        return user;
    }

    @POST
    public UserDTO createUser(UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PUT
    @Path("/{id}")
    public UserDTO updateUser(@PathParam("id") Long id, UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @DELETE
    @Path("/{id}")
    public void deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
    }

    @GET
    @Path("/me")
    public String me(@Context jakarta.ws.rs.core.SecurityContext context) {
        return "Usuário: " + context.getUserPrincipal().getName();
    }
}
