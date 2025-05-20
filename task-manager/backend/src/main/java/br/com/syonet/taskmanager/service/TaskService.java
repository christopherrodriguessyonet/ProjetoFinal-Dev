package br.com.syonet.taskmanager.service;

import br.com.syonet.taskmanager.dto.TaskDTO;
import br.com.syonet.taskmanager.entity.Task;
import br.com.syonet.taskmanager.repository.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import br.com.syonet.taskmanager.entity.User;
import java.time.LocalDateTime;
import java.time.LocalDate;



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
        dto.setStatus(task.getStatus());
        dto.setResponsavel(task.getResponsavel());
        dto.setCompleto(task.isCompleto());
        dto.setDataEntrega(task.getDataEntrega());
        return dto;
    }

    private Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
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

   ppublic List<TaskDTO> filtrarTarefas(String status, String dataInicial, String dataFinal, String userEmail) {
    return taskRepository.listAll().stream()
        .filter(task -> {
            // 1. Visibilidade: Admin vê tudo, usuário comum só o que ele criou
            boolean visivel = isAdmin(userEmail) ||
                (task.getResponsavel() != null && userEmail.equalsIgnoreCase(task.getResponsavel()));

            // 2. Filtro por status
            boolean correspondeStatus = (status == null || status.isBlank())
                || status.equalsIgnoreCase(task.getStatus());

            // 3. Filtro por data de entrega
            boolean dentroDoPeriodo = true;
            LocalDateTime dataEntrega = task.getDataEntrega();
            if (dataEntrega == null) return false;

            try {
                if (dataInicial != null && !dataInicial.isBlank()) {
                    LocalDateTime inicio = LocalDate.parse(dataInicial).atStartOfDay();
                    dentroDoPeriodo &= !dataEntrega.isBefore(inicio);
                }

                if (dataFinal != null && !dataFinal.isBlank()) {
                    LocalDateTime fim = LocalDate.parse(dataFinal).atTime(23, 59, 59);
                    dentroDoPeriodo &= !dataEntrega.isAfter(fim);
                }
            } catch (Exception e) {
                System.out.println("Erro ao converter datas: " + e.getMessage());
                return false;
            }

            return visivel && correspondeStatus && dentroDoPeriodo;
        })
        .map(this::toDTO)
        .collect(Collectors.toList());
}



}