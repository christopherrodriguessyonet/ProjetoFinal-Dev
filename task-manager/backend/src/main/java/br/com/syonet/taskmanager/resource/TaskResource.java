package br.com.syonet.taskmanager.resource;

import br.com.syonet.taskmanager.dto.TaskDTO;
import br.com.syonet.taskmanager.security.AuthService;
import br.com.syonet.taskmanager.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    AuthService authService;

    @Inject
    TaskService taskService;


    @POST
    @RolesAllowed({"ADMIN", "USER"})
    public Response criar(@Valid TaskDTO taskDTO) {
        TaskDTO novaTask = taskService.createTask(taskDTO);
        return Response.status(Response.Status.CREATED).entity(novaTask).build();
}


    // outros métodos (get, put, delete, etc.)
}