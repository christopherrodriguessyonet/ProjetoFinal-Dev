--Utilizado apenas para testes no quarkus dev  
-- Usuários com senha criptografada
INSERT INTO users (email, nome, senha, role)
VALUES ('admin@taskmanager.com', 'Administrador',
'$2a$12$5xbIW8TXT6l5B9QCrp5WlulQARuUQtSxLBvidtaPZcnizByq1hHBm', 'ADMIN');

INSERT INTO users (email, nome, senha, role)
VALUES ('user@taskmanager.com', 'Usuário',
'$2a$12$5xbIW8TXT6l5B9QCrp5WlulQARuUQtSxLBvidtaPZcnizByq1hHBm', 'USER');


