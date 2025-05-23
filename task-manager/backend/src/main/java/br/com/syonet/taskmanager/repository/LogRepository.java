package br.com.syonet.taskmanager.repository;

import br.com.syonet.taskmanager.entity.LogEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogRepository implements PanacheRepository<LogEntry> {
}
