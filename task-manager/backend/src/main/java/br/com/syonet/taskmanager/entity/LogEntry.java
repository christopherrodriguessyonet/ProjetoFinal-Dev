package br.com.syonet.taskmanager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String usuario;

    public String acao;

    public String entidade;

    public Long entidadeId;

    public LocalDateTime dataHora;
}
