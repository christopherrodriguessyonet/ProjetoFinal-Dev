-- Criar usuário admin inicial (senha: admin123)
INSERT INTO users (email, nome, senha, role)
VALUES ('admin@taskmanager.com', 'Administrador',
'$2a$12$3qmG/2.rivz9qV1BFdJh6.OYsUkNHD8.WNb/T72TwXRZWwWF.YyqG', 'ADMIN');

-- Criar usuário comum para testes (senha: user123)
INSERT INTO users (email, nome, senha, role)
VALUES ('user@taskmanager.com', 'Usuário Teste',
'$2a$12$jWO6mZhKVCDy1yqM0N/HU.O9U.dHbZX5W4P3spYGgGHE3ZUiYmQZi', 'USER');

-- Adicionar coluna data_entrega na tabela tasks, se não existir
ALTER TABLE tasks ADD COLUMN IF NOT EXISTS data_entrega TIMESTAMP;

-- Criar tarefa inicial
INSERT INTO tasks (titulo, descricao, status, responsavel, completo, data_entrega)
VALUES ('Primeira tarefa', 'Esta é uma tarefa criada automaticamente pelo import.sql', 'PENDENTE', 'admin@taskmanager.com', false, '2025-05-14');