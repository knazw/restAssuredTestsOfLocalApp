package org.cypress.example;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.cypress.example.model.User;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.hamcrest.Matchers.containsString;
import static org.slf4j.LoggerFactory.getLogger;

public class BaseTest {

    static final Logger log = getLogger(lookup().lookupClass());
    public static String baseUri = "http://localhost:3001";
    protected static String pathDataSeed = "/testData/seed";
    protected static String pathTransactions = "/transactions/";
    protected static String pathLikesTransaction = "/likes/";
    protected static String pathNotifications = "/notifications/";
    protected static String pathComments = "/comments/";
    protected static String pathUsers = "/users";
    protected static String pathLogin = "/login";
    public static String cookieValue = "";
    protected static String userId0;
    protected static String userId1;




    protected void clearData() {
        log.debug("clear data start");
        RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
            .when()
                .post(pathDataSeed)
            .then()
                .statusCode(200)
                .body(containsString("OK"));
        log.debug("clear data end");
    }

    protected static class SpecBuilder {
        public static RequestSpecification getRequestSpec(){
            return new RequestSpecBuilder().setBaseUri(baseUri)
                    .addHeader("Cookie",cookieValue)
                    .setContentType(ContentType.JSON).log(LogDetail.ALL).build();
        }

        public static RequestSpecification getRequestSpec(String cookieValueArg){
            return new RequestSpecBuilder().setBaseUri(baseUri)
                    .addHeader("Cookie",cookieValueArg)
                    .setContentType(ContentType.JSON).log(LogDetail.ALL).build();
        }
    }
}
