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
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;




@ApplicationScoped
public class TaskService {
    
    @Inject
    TaskRepository taskRepository;

    @Inject
    LogService logService;

    public List<TaskDTO> getAllTasks() {
        return taskRepository.listAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
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



    public TaskDTO getTask(Long id, String userEmail) throws WebApplicationException {
    Task task = taskRepository.findById(id);
    if (task == null) {
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    if (!userEmail.equalsIgnoreCase(task.getResponsavel()) && !isAdmin(userEmail)) {
        throw new WebApplicationException(Response.Status.FORBIDDEN);
    }

    return toDTO(task);
}


    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO, String userEmail) {
    taskDTO.setResponsavel(userEmail);
    Task task = toEntity(taskDTO);
    taskRepository.persist(task);
    logService.registrar(userEmail, "CRIAR", "TASK", task.getId());
    return toDTO(task);
}


    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO dto, String userEmail) {
    Task task = taskRepository.findById(id);
    if (task == null) {
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    task.setTitulo(dto.getTitulo());
    task.setDescricao(dto.getDescricao());
    task.setStatus(dto.getStatus());
    task.setResponsavel(dto.getResponsavel());
    task.setCompleto(dto.getCompleto());
    task.setDataEntrega(dto.getDataEntrega());

    logService.registrar(userEmail, "EDITAR", "TASK", task.getId()); // Log de edição

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

    private boolean isAdmin(String email) {
    User user = User.findByEmail(email);
    return user != null && user.role == User.UserRole.ADMIN;
}


   public List<TaskDTO> filtrarTarefas(String status, String dataInicial, String dataFinal, String usuario, String emailAutenticado) {
    return taskRepository.listAll().stream()
        .filter(task -> {
            // Visibilidade
            boolean isAdmin = isAdmin(emailAutenticado);
            String responsavel = task.getResponsavel();

            boolean visivel = isAdmin
            ? (usuario == null || usuario.isBlank() || (responsavel != null && responsavel.toLowerCase().contains(usuario.toLowerCase())))
            : (responsavel != null && responsavel.equalsIgnoreCase(emailAutenticado));


            // Filtro por status
            boolean correspondeStatus = (status == null || status.isBlank())
                || status.equalsIgnoreCase(task.getStatus());

            // Filtro por data
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
                return false;
            }

            return visivel && correspondeStatus && dentroDoPeriodo;
        })
        .map(this::toDTO)
        .collect(Collectors.toList());
}
}