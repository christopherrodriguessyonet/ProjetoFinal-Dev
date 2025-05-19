package br.com.syonet.taskmanager.service;

import br.com.syonet.taskmanager.dto.TaskDTO;
import br.com.syonet.taskmanager.entity.Task;
import br.com.syonet.taskmanager.repository.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaskService {
    
    @Inject
    TaskRepository taskRepository;

    public List<TaskDTO> getAllTasks() {
        return taskRepository.listAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = toEntity(taskDTO);
        taskRepository.persist(task);
        return toDTO(task);
    }

    private TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitulo(task.getTitulo());
        dto.setDescricao(task.getDescricao());
        dto.setStatus(dto.getStatus());
        dto.setResponsavel(dto.getResponsavel());
        dto.setCompleto(task.isCompleto());
        dto.setDataEntrega(task.getDataEntrega());
        return dto;
    }

    private Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setTitulo(dto.getTitulo());
        task.setDescricao(dto.getDescricao());
        task.setStatus(dto.getStatus());
        task.setResponsavel(dto.getResponsavel());
        task.setCompleto(dto.getCompleto());
        task.setDataEntrega(dto.getDataEntrega());
        return task;
}


    public List<TaskDTO> listTasks(String userEmail) {
    System.out.println("Buscando tarefas para: " + userEmail);
    List<TaskDTO> tarefas = taskRepository.listAll().stream()
        .filter(task -> userEmail.equalsIgnoreCase(task.getResponsavel()))
        .map(this::toDTO)
        .collect(Collectors.toList());
    System.out.println("Total de tarefas encontradas: " + tarefas.size());
    return tarefas;
}



    public TaskDTO getTask(Long id, String userEmail) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new jakarta.ws.rs.WebApplicationException(jakarta.ws.rs.core.Response.Status.NOT_FOUND);
        }
        // validar se o usuário tem permissão, se necessário
        return toDTO(task);
    }

        @Transactional
    public TaskDTO createTask(TaskDTO taskDTO, String userEmail) {
    taskDTO.setResponsavel(userEmail);
    return createTask(taskDTO);
}



    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO dto, String userEmail) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new jakarta.ws.rs.WebApplicationException(jakarta.ws.rs.core.Response.Status.NOT_FOUND);
        }

        task.setTitulo(dto.getTitulo());
        task.setDescricao(dto.getDescricao());
        task.setStatus(dto.getStatus());
        task.setResponsavel(dto.getResponsavel());
        task.setCompleto(dto.getCompleto());
        task.setDataEntrega(dto.getDataEntrega());

        return toDTO(task);
}


    @Transactional
    public void deleteTask(Long id, String userEmail) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new jakarta.ws.rs.WebApplicationException(jakarta.ws.rs.core.Response.Status.NOT_FOUND);
        }
        taskRepository.delete(task);
    }
}