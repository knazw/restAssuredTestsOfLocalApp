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
//import org.typescript.example.bdd.ApiConfig;

import static java.lang.invoke.MethodHandles.lookup;
import static org.hamcrest.Matchers.containsString;
import static org.slf4j.LoggerFactory.getLogger;

public class BaseTest {

    static final Logger log = getLogger(lookup().lookupClass());
    public static String baseUri = "http://localhost:3001";
    public static String baseUriTypescript = "http://localhost:3010";
    //public static String baseUriTypescript = ApiConfig.BASE_URI + ":"+ ApiConfig.PORT;
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

    // typescript tests
    protected static String setValuePath = "/set-value?key=b47n5ib5578juto97b573vy35y352n73m";
    protected static String computeOperationPath = "/compute?key=b47n5ib5578juto97b573vy35y352n73m";




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

    protected void clearDataAsync(int id) {
        new Thread(() -> {
            log.debug("clear data start id="+id);
            RestAssured.given(BaseTest.SpecBuilder.getRequestSpec())
                        .baseUri(baseUri)
                        .contentType(ContentType.JSON)
                    .when()
                        .post(pathDataSeed)
                    .then()
                        .statusCode(200)
                        .body(containsString("OK"));
            log.debug("clear data end id="+id);
        }).start();
    }
}
