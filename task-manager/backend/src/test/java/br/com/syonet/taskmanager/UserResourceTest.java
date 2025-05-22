package br.com.syonet.taskmanager;

import br.com.syonet.taskmanager.dto.LoginDTO;
import br.com.syonet.taskmanager.dto.UserDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

    private static String tokenAdmin;
    private static Long idUsuarioCriado;

    @BeforeAll
    public static void autenticarAdmin() {
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
    @Order(1)
    public void deveListarTodosOsUsuarios() {
        given()
            .header("Authorization", "Bearer " + tokenAdmin)
        .when()
            .get("/api/users")
        .then()
            .statusCode(200)
            .body("$", is(not(empty())));
    }

    @Test
    @Order(2)
    public void deveFiltrarUsuariosPorNome() {
        given()
            .header("Authorization", "Bearer " + tokenAdmin)
            .queryParam("usuario", "Administrador")
        .when()
            .get("/api/users")
        .then()
            .statusCode(200)
            .body("[0].nome", containsString("Administrador"));
    }

    @Test
    @Order(3)
    public void deveFiltrarUsuariosPorPerfil() {
        given()
            .header("Authorization", "Bearer " + tokenAdmin)
            .queryParam("perfil", "USER")
        .when()
            .get("/api/users")
        .then()
            .statusCode(200)
            .body("role", everyItem(equalTo("USER")));
    }

    @Test
    @Order(4)
    public void naoDevePermitirAcessoSemToken() {
        given()
        .when()
            .get("/api/users")
        .then()
            .statusCode(401);
    }

    @Test
    @Order(5)
    public void deveCriarUsuario() {
        UserDTO novoUsuario = new UserDTO();
        novoUsuario.setNome("Novo Usuário");
        novoUsuario.setEmail("teste.delecao@taskmanager.com");
        novoUsuario.setSenha("senha123");
        novoUsuario.setRole("USER");

        idUsuarioCriado = given()
            .header("Authorization", "Bearer " + tokenAdmin)
            .contentType(ContentType.JSON)
            .body(novoUsuario)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .body("nome", equalTo("Novo Usuário"))
            .extract().jsonPath().getLong("id");
        System.out.println("ID do usuário criado: " + idUsuarioCriado);
    }

    @Test
    @Order(6)
    public void deveAtualizarUsuario() {
        UserDTO usuarioAtualizado = new UserDTO();
        usuarioAtualizado.setNome("Usuário Atualizado");
        usuarioAtualizado.setEmail("teste.delecao@taskmanager.com");
        usuarioAtualizado.setSenha("novaSenha123");
        usuarioAtualizado.setRole("USER");

        given()
            .header("Authorization", "Bearer " + tokenAdmin)
            .contentType(ContentType.JSON)
            .body(usuarioAtualizado)
        .when()
            .put("/api/users/" + idUsuarioCriado)
        .then()
            .statusCode(200)
            .body("nome", equalTo("Usuário Atualizado"));
    }

    @Test
    @Order(99) // Executar por último - estava deletando o usuario admin
    public void deveDeletarUsuario() {
        given()
            .header("Authorization", "Bearer " + tokenAdmin)
        .when()
            .delete("/api/users/" + idUsuarioCriado)
        .then()
            .statusCode(204);
    }
}
