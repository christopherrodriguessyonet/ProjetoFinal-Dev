package br.com.syonet.taskmanager.resource;

import br.com.syonet.taskmanager.dto.TaskDTO;
import br.com.syonet.taskmanager.security.AuthService;
import br.com.syonet.taskmanager.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;



    @Path("/api/tasks")
    @Produces(MediaType.APPLICATION_JSON)
    public class TaskResource {

        @Inject
        TaskService taskService;

        @Inject
        AuthService authService;

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @RolesAllowed({"ADMIN", "USER"})
        public Response criar(@Valid TaskDTO taskDTO, @Context SecurityContext context) {
            String emailUsuario = context.getUserPrincipal().getName();
            TaskDTO novaTask = taskService.createTask(taskDTO, emailUsuario);
            return Response.status(Response.Status.CREATED).entity(novaTask).build();
        }

        @GET
        @Path("")
        @RolesAllowed({"ADMIN"})
        public Response listarTodas(@Context SecurityContext context) {
        return Response.ok(taskService.getAllTasks()).build();
    }
        @DELETE
        @Path("/{id}")
        @RolesAllowed({"ADMIN", "USER"})
        public Response deletar(@PathParam("id") Long id, @Context SecurityContext context) {
            String emailUsuario = context.getUserPrincipal().getName();
            taskService.deleteTask(id, emailUsuario);
              return Response.noContent().build();
    }
        @PUT
        @Path("/{id}")
        @Consumes(MediaType.APPLICATION_JSON)
        @RolesAllowed({"ADMIN", "USER"})
        public Response atualizar(@PathParam("id") Long id, @Valid TaskDTO dto, @Context SecurityContext context) {
            String emailUsuario = context.getUserPrincipal().getName();
            TaskDTO atualizada = taskService.updateTask(id, dto, emailUsuario);
            return Response.ok(atualizada).build();
}
        @GET
        @Path("/minhas")
        @RolesAllowed({"USER", "ADMIN"})
        public Response listarMinhas(@Context SecurityContext context) {
            String emailUsuario = context.getUserPrincipal().getName();
            System.out.println("Usuário autenticado: " + emailUsuario);
            return Response.ok(taskService.listTasks(emailUsuario)).build();
    }

    @GET
    @Path("/filtro")
    @RolesAllowed({"ADMIN", "USER"})
    public Response filtrarTarefas(
        @QueryParam("status") String status,
        @QueryParam("dataInicial") String dataInicial,
        @QueryParam("dataFinal") String dataFinal,
        @QueryParam("usuario") String usuario,
        @Context SecurityContext context
    ) {
        String emailUsuario = context.getUserPrincipal().getName();
        return Response.ok(taskService.filtrarTarefas(status, dataInicial, dataFinal, emailUsuario)).build();
    }
    }