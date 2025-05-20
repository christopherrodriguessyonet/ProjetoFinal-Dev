package br.com.syonet.taskmanager.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskDTO {
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    public String titulo;

    public String descricao;

    @NotBlank(message = "O status é obrigatório")
    public String status;

    @NotBlank(message = "O responsável é obrigatório")
    public String responsavel;

    @NotNull(message = "O campo 'completo' é obrigatório")
    public Boolean completo;

    @NotNull(message = "A data de entrega é obrigatória")
    @FutureOrPresent(message = "A data de entrega não pode estar no passado")
    public LocalDateTime dataEntrega;
}