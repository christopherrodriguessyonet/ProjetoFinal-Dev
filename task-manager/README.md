# Task Manager

Sistema de gerenciamento de tarefas com autenticação e controle de acesso por perfil de usuário.

## Tecnologias utilizadas

### Backend
- Java 17
- Quarkus
- Hibernate ORM com Panache
- PostgreSQL
- JWT (autenticação)
- RESTEasy Reactive
- Docker + Docker Compose

### Frontend
- React
- TypeScript
- Material UI (MUI)
- Axios
- React Router

## Funcionalidades

### Usuários
- Login e cadastro com validação
- Acesso controlado por perfil (`ADMIN`, `USER`)
- ADMIN pode listar, criar, editar e excluir usuários

### Tarefas
- Criação, listagem, edição e exclusão de tarefas
- Filtro por status, data e responsável
- Usuário comum vê apenas suas tarefas
- ADMIN vê todas as tarefas

### Segurança
- Autenticação com JWT
- Refresh token implementado
- Controle de rotas no frontend com base na role

### Testes
Para rodar os testes precisará comentar a linha do application.properties em "quarkus.datasource.jdbc.url=jdbc:postgresql://postgres:5432/taskmanager"
e executr ./mvnw quarkus:dev

### Inicialização do projeto
Para executar o projeto, basta estar na pasta principal e executar ./start.sh
Para realizar os testes como usuário, crie um novo usuário a partir da conta de administrador: admin@taskmanager.com (senha: admin123).
Para executar o projeto, é necessário ter instalado o Node.js na versão 20.19.2 e o Java na versão 17.
Adicione a pasta principal do projeto (/ProjetoFinal-dev/taskmanager) no recurso "File Share" do Docker Desktop, para que o projeto seja localizado corretamente.
Acesse a URL do frontend utilizando uma guia anônima no navegador.
