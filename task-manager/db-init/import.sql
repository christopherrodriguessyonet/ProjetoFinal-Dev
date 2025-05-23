-- Criação da tabela users
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Criação da tabela tasks
CREATE TABLE IF NOT EXISTS tasks (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    status VARCHAR(50),
    responsavel VARCHAR(255),
    completo BOOLEAN NOT NULL DEFAULT false,
    data_entrega TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logs (
    id SERIAL PRIMARY KEY,
    usuario VARCHAR(255),
    acao VARCHAR(100),
    entidade VARCHAR(100),
    entidade_id BIGINT,
    data_hora TIMESTAMP
);


-- Criar usuário admin inicial (senha: admin123, hash gerado pelo endpoint /api/auth/hash)
INSERT INTO users (email, nome, senha, role)
VALUES ('admin@taskmanager.com', 'Administrador',
'$2a$12$5xbIW8TXT6l5B9QCrp5WlulQARuUQtSxLBvidtaPZcnizByq1hHBm', 'ADMIN');

-- Criar usuário comum para testes (senha: user123, hash gerado pelo endpoint /api/auth/hash)
INSERT INTO users (email, nome, senha, role)
VALUES ('user@taskmanager.com', 'Usuário Teste',
'$2a$12$jWO6mZhKVCDy1yqM0N/HU.O9U.dHbZX5W4P3spYGgGHE3ZUiYmQZi', 'USER');

-- Criar tarefa inicial
INSERT INTO tasks (titulo, descricao, status, responsavel, completo, data_entrega)
VALUES ('Primeira tarefa', 'Esta é uma tarefa criada automaticamente pelo import.sql', 'PENDENTE', 'admin@taskmanager.com', false, '2025-05-14'); 