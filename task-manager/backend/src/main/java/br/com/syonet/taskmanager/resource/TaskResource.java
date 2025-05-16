package br.com.syonet.taskmanager.resource;

import br.com.syonet.taskmanager.dto.TaskDTO;
import br.com.syonet.taskmanager.entity.Task;
import br.com.syonet.taskmanager.entity.User;
import br.com.syonet.taskmanager.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/api/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"USER", "ADMIN"})
public class TaskResource {

    @Inject
    TaskService taskService;

    @GET
    public List<TaskDTO> listTasks() {
        List<TaskDTO> tasks = taskService.listTasks(null);
        if (tasks == null || tasks.isEmpty()) {
            throw new WebApplicationException("Nenhuma tarefa encontrada", 404);
        }
        return tasks;
    }

    @GET
    @Path("/{id}")
    public TaskDTO getTask(@PathParam("id") Long id) {
        TaskDTO task = taskService.getTask(id, null);
        if (task == null) {
            throw new WebApplicationException("Tarefa não encontrada", 404);
        }
        return task;
    }

    @POST
    public TaskDTO createTask(TaskDTO taskDTO) {
        try {
            return taskService.createTask(taskDTO, null);
        } catch (Exception e) {
            throw new WebApplicationException("Erro ao criar tarefa: " + e.getMessage(), 400);
        }
    }

    @PUT
    @Path("/{id}")
    public TaskDTO updateTask(@PathParam("id") Long id, TaskDTO taskDTO) {
        try {
            return taskService.updateTask(id, taskDTO, null);
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new WebApplicationException("Erro ao atualizar tarefa: " + e.getMessage(), 400);
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteTask(@PathParam("id") Long id) {
        try {
            taskService.deleteTask(id, null);
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new WebApplicationException("Erro ao deletar tarefa: " + e.getMessage(), 400);
        }
    }
}