package org.cypress.example;

import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.cypress.example.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;

public class LoginTests extends BaseTest{

    @BeforeAll
    public static void beforeClss() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter());
    }

    @AfterEach
    public void afterEach() {
        clearData();
    }

    @Test
    public void loginTestShouldFailWith401WhenUserDoesNotExist() {
        String requestBody = "{\n" +
                "    \"type\": \"LOGIN\",\n" +
                "    \"username\": \"username\",\n" +
                "    \"password\": \"12345678\"\n" +
                "}";

        RestAssured.given()
                .baseUri(baseUri)
                .body(requestBody)
                .contentType(ContentType.JSON)
            .when()
                .post("/login")
            .then()
                .statusCode(401)
                .body(containsString("Unauthorized"));
    }

    @Test
    public void loginTestShouldFailWith400WhenPasswordIsEmpty() {
        String requestBody = "{\n" +
                "    \"type\": \"LOGIN\",\n" +
                "    \"username\": \"username\",\n" +
                "    \"password\": \"\"\n" +
                "}";

        RestAssured.given()
                .baseUri(baseUri)
                .body(requestBody)
                .contentType(ContentType.JSON)
            .when()
                .post("/login")
            .then()
                .statusCode(400)
                .body(containsString("Bad Request"));

    }

    @Test
    public void loginTestShouldFailWith400WhenPasswordIsNotProvided() {
        String requestBody = "{\n" +
                "    \"type\": \"LOGIN\",\n" +
                "    \"username\": \"username\"\n" +
                "}";

        RestAssured.given()
                .baseUri(baseUri)
                .body(requestBody)
                .contentType(ContentType.JSON)
            .when()
                .post("/login")
            .then()
                .statusCode(400)
                .body(containsString("Bad Request"));

    }

    @Test
    public void loginTestShouldFailWith400WhenPasswordAndLoginIsNotProvided() {
        String requestBody = "{\n" +
                "    \"type\": \"LOGIN\"\n" +
                "}";

        RestAssured.given()
                .baseUri(baseUri)
                .body(requestBody)
                .contentType(ContentType.JSON)
            .when()
                .post("/login")
            .then()
                .statusCode(400)
                .body(containsString("Bad Request"));

    }

    @Test
    public void loginTestShouldFailWith400WhenEmptyBody() {
        RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
            .when()
                .post("/login")
            .then()
                .statusCode(400)
                .body(containsString("Bad Request"));
    }

    @Test
    public void shouldLoginWhenUserExists() {
        // GIVEN
        User user = new User();
        user.setFirstName("Adam");
        user.setLastName("Nowak");
        user.setUsername("username");
        user.setPassword("12345678");
        user.setConfirmPassword("12345678");

        // WHEN
        RestAssured.given(SpecBuilder.getRequestSpec())
                .body(user)
            .when()
                .post("/users")
            .then()
                .statusCode(201)
                .assertThat()
                .body(matchesJsonSchemaInClasspath("createdUser.json"));

        // THEN
        RestAssured.given(SpecBuilder.getRequestSpec())
                .body(user)
            .when()
                .post("/login")
            .then()
                .statusCode(200)
                .assertThat()
                .body(matchesJsonSchemaInClasspath("loggedUser.json"));

    }

}
