package org.typescript.example.bdd;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.cypress.example.BaseTest;
import org.cypress.example.bdd.StepsData;
import org.cypress.example.model.UserCreated;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.typescript.example.MathOperation;
import org.typescript.example.OperationResult;
import org.typescript.example.ValueX;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;


public class LocalTypescriptRestApiBDDClass extends BaseTest {
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    //private static String configPath = "configs//application.properties";
    static final Logger log = getLogger(lookup().lookupClass());
    private LocalDateTime scenarioStartTime;

    private StepsDataTS stepsDataTS;
    /*
    private static WireMockServer wireMockServer;
    private static final int WIREMOCK_PORT = 8089;

    static {
        // Initialize WireMock server
        wireMockServer = new WireMockServer(options().port(WIREMOCK_PORT));
        wireMockServer.start();

        // Configure Rest-Assured to use WireMock server
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = WIREMOCK_PORT;

        // Configure WireMock client
        WireMock.configureFor("localhost", WIREMOCK_PORT);
    }

     */


    public LocalTypescriptRestApiBDDClass(StepsDataTS stepsDataTS) {
        this.stepsDataTS = stepsDataTS;
    }


    @Given("Set value {string}: {int}")
    public void FollowingUser(String valueName, int value) {

        ValueX valueX = new ValueX(valueName, value);
        switch (valueName) {
            case "A":
                stepsDataTS.valueA = valueX;
                break;
            case "B":
                stepsDataTS.valueB = valueX;
                break;
        }

        stepsDataTS.validatableResponse = RestAssured
                .given(SpecBuilder.getRequestSpec())
                .baseUri(baseUriTypescript)
                .body(valueX)
                .contentType(ContentType.JSON)
                .when()
                .post(setValuePath)
                .then();

    }

    @When("Compute {string} operation")
    public void computeOperation(String mathematicalOperation) {
        MathOperation mathematicalOperationX = new MathOperation(mathematicalOperation);
        mathematicalOperationX.setMathematicalOperation(mathematicalOperation);

        stepsDataTS.validatableResponse = RestAssured
                .given(SpecBuilder.getRequestSpec())
                    .baseUri(baseUriTypescript)
                    .body(mathematicalOperationX)
                    .contentType(ContentType.JSON)
                .when()
                    .post(computeOperationPath)
                .then();
    }

    @Then("{int} response code is received from endpoint")
    public void responseCodeIsReceivedFromEndpoint(int arg0) {

        stepsDataTS.validatableResponse.statusCode(arg0);
    }

    @And("Result is: {int}")
    public void resultIsResult(int result) {
        Response response = stepsDataTS.validatableResponse.extract()
                .response();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OperationResult operationResult = objectMapper.convertValue(response.jsonPath().get(), OperationResult.class);
        log.debug("userCreated: "+operationResult.toString());

        Assertions.assertTrue(operationResult.getValueSet() == result);
    }

    @And("Json in response body matches {string}")
    public void jsonInResponseBodyMatchesComputedValueJson(String jsonName) {
        log.debug("jsonName: "+jsonName);
        this.stepsDataTS.validatableResponse.assertThat()
                .body(matchesJsonSchemaInClasspath("FilesTSApp/"+jsonName));
    }

    @And("Message is : {string}")
    public void messageIs(String message) {
        log.debug("message: "+message);
        Response response = this.stepsDataTS.validatableResponse.extract().response();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OperationResult operationResult = objectMapper.convertValue(response.jsonPath().get(), OperationResult.class);
        log.debug("operationResult: "+operationResult.toString());

        Assertions.assertTrue(operationResult.getMessage().equals(message)); //Assertions.assertEquals(operationResult.getMessage(), message);
    }

    @And("It is possible to send {string} request to endpoint {string} with key {string}")
    public void itIsPossibleToSendRequestToEndpointWithKey(String method, String endpoint, String key) {

        RequestSpecification request = RestAssured
                .given(SpecBuilder.getRequestSpec())
                    .log().all()
                    .baseUri(baseUriTypescript)
                    .contentType(ContentType.JSON)
                .when();

        //log.debug("endpointWithKey: "+endpoint + key);
        //String endpointWithKey = URLEncoder.encode(endpoint + key, StandardCharsets.UTF_8);
        //String endpointWithKey = URLDecoder.decode(endpoint + key, StandardCharsets.UTF_8);
        //log.debug("endpointWithKey: "+endpointWithKey);

        RequestsHelpers requestsHelpers = new RequestsHelpers();
        ValidatableResponse validatableResponse = requestsHelpers.getRequestSpec(request, method, endpoint + key);
        stepsDataTS.validatableResponse = validatableResponse;


    }

    @And("{int} response code is received from TS endpoint")
    public void statuscoderesultgetResponseCodeIsReceivedFromTSEndpoint(int statusCodeResultGET) {
        stepsDataTS.validatableResponse.statusCode(statusCodeResultGET);
    }

    @And("Sleep for {int} seconds")
    public void sleepForSeconds(int sleepTime) throws InterruptedException {
        try {
            Thread.sleep(sleepTime * 1000);
        }
        catch (InterruptedException e) {
            log.debug("Sleep interrupted "+e.getMessage());
            e.printStackTrace();
        }
    }
}
