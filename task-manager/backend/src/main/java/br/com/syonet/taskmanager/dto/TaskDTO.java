package br.com.syonet.taskmanager.dto;

import lombok.Data; 
import java.time.LocalDateTime;
@Data
public class TaskDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String status;
    private String responsavel;
    private boolean completo;
    private LocalDateTime dataEntrega;
}