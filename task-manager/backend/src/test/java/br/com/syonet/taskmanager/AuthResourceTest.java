package br.com.syonet.taskmanager;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import br.com.syonet.taskmanager.dto.LoginDTO;
import br.com.syonet.taskmanager.dto.UserDTO;

@QuarkusTest
public class AuthResourceTest {

    @Test
    public void deveLogarComSucesso() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"email\": \"admin@taskmanager.com\", \"senha\": \"admin123\" }")
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .body("accessToken", notNullValue());
    }
    @Test
    public void deveFalharLoginComSenhaIncorreta() {
        given()
            .contentType(ContentType.JSON)
            .body(new LoginDTO("admin@taskmanager.com", "senhaErrada"))
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(401);
    }

    @Test
    public void deveFalharLoginComUsuarioInexistente() {
        given()
            .contentType(ContentType.JSON)
            .body(new LoginDTO("naoexiste@taskmanager.com", "admin"))
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(401);
    }

    @Test
    public void deveRegistrarNovoUsuarioComSucesso() {
        UserDTO novoUsuario = new UserDTO();
        novoUsuario.setEmail("novo@teste.com");
        novoUsuario.setNome("Novo Usuário");
        novoUsuario.setSenha("12345678");
        novoUsuario.setRole("USER");

        given()
            .contentType(ContentType.JSON)
            .body(novoUsuario)
        .when()
            .post("/api/auth/register")
        .then()
            .statusCode(200)
            .body("accessToken", notNullValue())
            .body("refreshToken", notNullValue());
    }

    @Test
    public void deveFalharAoRegistrarEmailExistente() {
        UserDTO usuarioExistente = new UserDTO();
        usuarioExistente.setEmail("admin@taskmanager.com");
        usuarioExistente.setNome("admin");
        usuarioExistente.setSenha("12345678");
        usuarioExistente.setRole("USER");

        given()
            .contentType(ContentType.JSON)
            .body(usuarioExistente)
        .when()
            .post("/api/auth/register")
        .then()
            .statusCode(409);
    }


}
