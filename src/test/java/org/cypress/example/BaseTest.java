package org.cypress.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.cypress.example.models.User;

import static org.hamcrest.Matchers.containsString;

public class BaseTest {
    public static String baseUri = "http://localhost:3001";
    protected static String pathDataSeed = "/testData/seed";
    protected static String pathTransactions = "/transactions";
    protected static String pathLikesTransaction = "/likes/";
    protected static String pathNotifications = "/notifications/";
    protected static String pathComments = "/comments/";
    public static String cookieValue = "";
    protected static String userId0;
    protected static String userId1;
    protected static String loggedUserId;
    protected static User user;
    protected static User user1;

    protected static void createUsers() {
        user = new User();
        user.setFirstName("Adam");
        user.setLastName("Nowak");
        user.setUsername("username");
        user.setPassword("12345678");
        user.setConfirmPassword("12345678");

        user1 = new User();
        user1.setFirstName("Jan");
        user1.setLastName("Kowalski");
        user1.setUsername("username1");
        user1.setPassword("12345678");
        user1.setConfirmPassword("12345678");
    }

    protected void addUserAndLogin() {
        RestAssured.given()
                .baseUri(baseUri)
                .body(user)
                .contentType(ContentType.JSON)
            .when()
                .post("/users")
            .then()
                .statusCode(201);

        //cookieValue
        Response loginResponse = RestAssured.given()
                .baseUri(baseUri)
                .body(user)
                .contentType(ContentType.JSON)
            .when()
                .post("/login")
            .then()
                .statusCode(200)
                .extract().response();
        JsonPath jsonPathEvaluator = jsonPathEvaluator = loginResponse.jsonPath();

        System.out.println("user.id received from loginResponse " + jsonPathEvaluator.get("user.id"));
        loggedUserId = jsonPathEvaluator.get("user.id");
        cookieValue = loginResponse.header("Set-Cookie");


        String[] cookieHeaderArray = cookieValue.split(";");
        cookieValue = cookieHeaderArray[0];

        Response response = RestAssured.given()
                .baseUri(baseUri)
                .body(user1)
                .contentType(ContentType.JSON)
            .when()
                .post("/users")
            .then()
                .statusCode(201)
                .extract().response();

        jsonPathEvaluator = response.jsonPath();


        System.out.println("user.id received from Response " + jsonPathEvaluator.get("user.id"));
        System.out.println("user.username received from Response " + jsonPathEvaluator.get("user.username"));
        userId1 = jsonPathEvaluator.get("user.id");

        response = RestAssured.given()
                .baseUri(baseUri)
                .body(user)
                .contentType(ContentType.JSON)
            .when()
                .post("/users")
            .then()
                .statusCode(201)
                .extract().response();

        jsonPathEvaluator = response.jsonPath();

        System.out.println("user.id received from Response " + jsonPathEvaluator.get("user.id"));
        System.out.println("user.username received from Response " + jsonPathEvaluator.get("user.username"));
        userId0 = jsonPathEvaluator.get("user.id");
    }

    protected void clearData() {
        RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
            .when()
                .post(pathDataSeed)
            .then()
                .statusCode(200)
                .body(containsString("OK"));
    }

    static class SpecBuilder {
        static String token="";
        public static RequestSpecification getRequestSpec(){
            return new RequestSpecBuilder().setBaseUri(baseUri)
                    .addHeader("Cookie",cookieValue)
                    .setContentType(ContentType.JSON).log(LogDetail.ALL).build();
        };

        public static ResponseSpecification getResponseSpec(){
            return new ResponseSpecBuilder().
                    log(LogDetail.ALL).
                    build();
        }
    }
}
