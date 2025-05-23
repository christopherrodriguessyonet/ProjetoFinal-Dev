package br.com.syonet.taskmanager.service;

import br.com.syonet.taskmanager.entity.LogEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
public class LogService implements PanacheRepository<LogEntry> {

    @Transactional
    public void registrar(String usuario, String acao, String entidade, Long entidadeId) {
        LogEntry log = new LogEntry();
        log.usuario = usuario;
        log.acao = acao;
        log.entidade = entidade;
        log.entidadeId = entidadeId;
        log.dataHora = LocalDateTime.now();

        persist(log);
    }
}
