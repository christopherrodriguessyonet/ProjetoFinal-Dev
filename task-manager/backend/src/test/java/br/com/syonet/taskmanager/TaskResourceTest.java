package br.com.syonet.taskmanager;

import br.com.syonet.taskmanager.dto.LoginDTO;
import br.com.syonet.taskmanager.dto.TaskDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TaskResourceTest {

    private String tokenUser;
    private String tokenAdmin;

    @BeforeEach
    public void obterTokens() {

        // Login como ADMIN
        tokenAdmin = given()
            .contentType(ContentType.JSON)
            .body(new LoginDTO("admin@taskmanager.com", "admin123"))
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .extract().path("accessToken");
    }

    @Test
    public void deveCriarTarefaComUsuarioAutenticado() {
        TaskDTO novaTarefa = new TaskDTO();
        novaTarefa.setTitulo("Tarefa teste");
        novaTarefa.setDescricao("Descrição da tarefa teste");
        novaTarefa.setStatus("PENDENTE");
        novaTarefa.setResponsavel("chris@taskmanager.com");
        novaTarefa.setCompleto(false);
        novaTarefa.setDataEntrega(LocalDateTime.now().plusDays(1)); 

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + tokenAdmin)
            .body(novaTarefa)
        .when()
            .post("/api/tasks")
        .then()
            .statusCode(201)
            .body("titulo", equalTo("Tarefa teste"));
    }

    @Test
    public void deveListarTarefasDoUsuario() {
        given()
            .header("Authorization", "Bearer " + tokenAdmin)
        .when()
            .get("/api/tasks/minhas")
        .then()
            .statusCode(200)
            .body("$", is(notNullValue()));
    }

    @Test
    public void adminDeveListarTodasAsTarefas() {
        given()
            .header("Authorization", "Bearer " + tokenAdmin)
        .when()
            .get("/api/tasks")
        .then()
            .statusCode(200)
            .body("$", is(notNullValue()));
    }

    @Test
    public void deveFiltrarTarefasPorStatus() {
        given()
            .header("Authorization", "Bearer " + tokenAdmin)
            .queryParam("status", "PENDENTE")
        .when()
            .get("/api/tasks/filtro")
        .then()
            .statusCode(200)
            .body("$", is(notNullValue()));
    }

    @Test
    public void naoDevePermitirCriarTarefaSemToken() {
        TaskDTO novaTarefa = new TaskDTO();
        novaTarefa.setTitulo("Tarefa sem token");
        novaTarefa.setDescricao("Deveria falhar");
        novaTarefa.setStatus("PENDENTE");

        given()
            .contentType(ContentType.JSON)
            .body(novaTarefa)
        .when()
            .post("/api/tasks")
        .then()
            .statusCode(401);
    }
}
