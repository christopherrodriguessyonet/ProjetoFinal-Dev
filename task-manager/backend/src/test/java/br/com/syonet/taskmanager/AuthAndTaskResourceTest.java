package br.com.syonet.taskmanager;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AuthAndTaskResourceTest {

    @Test
    public void loginComAdminEListaTarefas() {
        String token =
            given()
                .contentType(ContentType.JSON)
                .body("{\"email\": \"admin@taskmanager.com\", \"senha\": \"admin123\"}")
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract()
                .path("token");

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/api/tasks")
        .then()
            .statusCode(200)
            .body("$", is(not(empty())));
    }
}
