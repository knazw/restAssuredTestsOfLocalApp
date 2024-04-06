package org.cypress.example.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.cypress.example.BaseTest;
import org.cypress.example.model.User;
import org.cypress.example.model.UserCreated;
import org.dataProviders.JsonDataReader;
import org.dataProviders.PropertiesStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.lang.invoke.MethodHandles.lookup;
import static org.hamcrest.Matchers.containsString;
import static org.slf4j.LoggerFactory.getLogger;

public class FirstBddClass extends BaseTest{

    private static String configPath = "configs//application.properties";
    static final Logger log = getLogger(lookup().lookupClass());

    private static List<User> users;
    private User user;
    ValidatableResponse loginResponse;
    ValidatableResponse createUserResponse;

    @After
    public void afterEach() {
        clearData();
    }

    @Before
    public void beforeEach() {
        clearData();
    }


    @BeforeAll
    public static void setupAll() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter(), new AllureRestAssured());


        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            properties.load(reader);

            PropertiesStorage propertiesStorage = PropertiesStorage.getInstance("");
            propertiesStorage.setProperties(properties);
        }
        catch (IOException e) {
            throw new RuntimeException("Properties file not found at path : " + configPath);
        }
    }



    @Given("I have the following user {string}")
    public void IHaveTheFollowingCredentials(String username) {
        JsonDataReader jsonDataReader = new JsonDataReader();
        user = jsonDataReader.getUserByUsername(username);
    }

    @When("When I start to login with credentials")
    public void WhenIStartToLoginWithCredentials() {
        loginResponse = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec())
                    .baseUri(baseUri)
                    .body(user)
                    .contentType(ContentType.JSON)
                .when()
                    .post(pathLogin)
                .then();
    }

    @When("I start to login with credentials from file {string}")
    public void WhenIStartToLoginWithCredentialsFromFile(String fileName) {
        String jsonInvalidUser = JsonDataReader.getInvalidUser(fileName);

        loginResponse = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec())
                    .baseUri(baseUri)
                    .body(jsonInvalidUser)
                    .contentType(ContentType.JSON)
                .when()
                    .post(pathLogin)
                .then();
    }

    @When("I start to login with no credentials")
    public void IStartToLoginWithNoCredentials() {
        loginResponse = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec())
                    .baseUri(baseUri)
                    .contentType(ContentType.JSON)
                .when()
                    .post(pathLogin)
                .then();
    }


    @Then("I receive {int} response code")
    public void IReceiveResponseCode(int statusCode) {
        loginResponse.statusCode(statusCode);
    }

    @And("Response message {string}")
    public void ResponseMessage(String string) {
        loginResponse.body(containsString(string));
    }

    /*
    @Then("I receive 401 response code")
    public void IReceive401ResponseCode() {
        Response response = loginResponse.statusCode(401)
                .extract()
                .response();
        log.debug("response body:"+response.body().prettyPrint());

    }
    */

    @And("{string} is created")
    public void UserIsCreated(String username) {
//        createUserResponse
         Response response       = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec())
                    .baseUri(baseUri)
                    .body(user)
                    .contentType(ContentType.JSON)
                .when()
                    .post(pathUsers)
                .then()
                    .statusCode(201)
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath("createdUser.json"))
                    .extract()
                    .response();
        ObjectMapper objectMapper = new ObjectMapper();
        UserCreated userCreated = objectMapper.convertValue(response.jsonPath().get("user"),UserCreated.class);
        log.debug("userCreated: "+userCreated.toString());

        Assertions.assertEquals(user.getFirstName(), userCreated.getFirstName());
        Assertions.assertEquals(user.getLastName(), userCreated.getLastName());
        Assertions.assertEquals(user.getUsername(), userCreated.getUsername());
        Assertions.assertTrue(userCreated.getPassword() != null); // ?
        Assertions.assertTrue(UUID.fromString(userCreated.getUuid()) != null);

    }

    /*
    @Then("I receive 200 response code")
    public void IReceive200ResponseCode() {
        Response response = loginResponse.statusCode(200)
                .assertThat()
                .body(matchesJsonSchemaInClasspath("loggedUser.json"))
                .extract()
                .response();

        ObjectMapper objectMapper = new ObjectMapper();
        UserCreated userCreated = objectMapper.convertValue(response.jsonPath().get("user"),UserCreated.class); // ?
        log.debug("userCreated: "+userCreated.toString());

        Assertions.assertEquals(user.getFirstName(), userCreated.getFirstName());
        Assertions.assertEquals(user.getLastName(), userCreated.getLastName());
        Assertions.assertEquals(user.getUsername(), userCreated.getUsername());
        Assertions.assertTrue(userCreated.getPassword() != null); //
        Assertions.assertTrue(UUID.fromString(userCreated.getUuid()) != null);

    }

     */

    @And("Correct user object")
    public void CorrectUserObject() {
        Response response = loginResponse.assertThat()
                .body(matchesJsonSchemaInClasspath("loggedUser.json"))
                .extract()
                .response();

        ObjectMapper objectMapper = new ObjectMapper();
        UserCreated userCreated = objectMapper.convertValue(response.jsonPath().get("user"),UserCreated.class); // ?
        log.debug("userCreated: "+userCreated.toString());

        Assertions.assertEquals(user.getFirstName(), userCreated.getFirstName());
        Assertions.assertEquals(user.getLastName(), userCreated.getLastName());
        Assertions.assertEquals(user.getUsername(), userCreated.getUsername());
        Assertions.assertTrue(userCreated.getPassword() != null); //
        Assertions.assertTrue(UUID.fromString(userCreated.getUuid()) != null);
    }

}
