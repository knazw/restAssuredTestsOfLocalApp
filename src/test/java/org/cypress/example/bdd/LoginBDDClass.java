package org.cypress.example.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.cypress.example.BaseTest;
import org.cypress.example.model.User;
import org.cypress.example.model.UserCreated;
import org.cypress.example.utils.AllureLoggerUtils;
import org.dataProviders.JsonDataReader;
import org.dataProviders.PropertiesStorage;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.typescript.example.bdd.ApiConfig;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.lang.invoke.MethodHandles.lookup;
import static org.hamcrest.Matchers.containsString;
import static org.slf4j.LoggerFactory.getLogger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginBDDClass extends BaseTest{

    private static String configPath = "configs//application.properties";
    static final Logger log = getLogger(lookup().lookupClass());
    private LocalDateTime scenarioStartTime;
    private static WireMockServer wireMockServer;

    private StepsData stepsData;

    public LoginBDDClass(StepsData stepsData) {
        this.stepsData = stepsData;
    }

    @After
    public void afterEach(Scenario scenario) {
        log.debug("\n\n\n============after each============\n\n\n");
//        clearData();
        clearDataAsync(0);
        clearDataAsync(1);
        clearDataAsync(2);

        LocalDateTime endTime = LocalDateTime.now();
        String endTimestamp = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        long durationMs = java.time.Duration.between(scenarioStartTime, endTime).toMillis();
        log.info("[{}] Finished scenario: {} (Status: {}, Duration: {} ms)",
                endTimestamp, scenario.getName(), scenario.getStatus(), durationMs);

        String summary = String.format(
                "Scenario: %s%nEnd Time: %s%nStatus: %s%nDuration: %d ms%nStart Time: %s",
                scenario.getName(),
                endTimestamp,
                scenario.getStatus(),
                durationMs,
                scenarioStartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        );
        Allure.addAttachment("Scenario Summary", "text/plain", summary);

        if (scenario.isFailed()) {
            AllureLoggerUtils.addTimestampedLog("Scenario failed - capturing failure details");
        }
    }

    @Before
    public void beforeEach(Scenario scenario ) {
        log.debug("\n\n\n============before each============\n\n\n");
        scenarioStartTime = LocalDateTime.now();
        String startTimestamp = scenarioStartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        log.info("[{}] Starting scenario: {}", startTimestamp, scenario.getName());

        String scenarioInfo = String.format(
                "Scenario: %s%nStart Time: %s%nTags: %s",
                scenario.getName(),
                startTimestamp,
                scenario.getSourceTagNames()
        );
        Allure.addAttachment("Scenario Start Info", "text/plain", scenarioInfo);
        AllureLoggerUtils.addTimestampedLog("Scenario execution started: " + scenario.getName());
        clearData();
    }


    @BeforeAll
    public static void setupAll() {
        boolean useMock = Boolean.parseBoolean(System.getProperty("api.mock", "true"));
        if (useMock) {
            wireMockServer = new WireMockServer(ApiConfig.PORT);
            wireMockServer.start();
            // register stubs...
        }

        if(RestAssured.filters().size() == 0) {
            RestAssured.filters(new AllureRestAssured());
        }
//        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter(), new AllureRestAssured());


        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            properties.load(reader);

            PropertiesStorage propertiesStorage = PropertiesStorage.getInstance();
            propertiesStorage.setProperties(properties);
        }
        catch (IOException e) {
            throw new RuntimeException("Properties file not found at path : " + configPath);
        }
    }




    @Given("Following user {string}")
    public void FollowingUser(String username) {
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyPort", "8888");
        JsonDataReader jsonDataReader = new JsonDataReader();
        stepsData.user = jsonDataReader.getUserByUsername(username);
    }

    @When("{string} starts to login with credentials")
    public void WhenIStartToLoginWithCredentials(String username) {
        stepsData.validatableResponse = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec())
                    .baseUri(baseUri)
                    .body(stepsData.user)
                    .contentType(ContentType.JSON)
                .when()
                    .post(pathLogin)
                .then();

    }

    @When("{string} starts to login with credentials from file {string}")
    public void WhenIStartToLoginWithCredentialsFromFile(String username, String fileName) {
        String jsonInvalidUser = JsonDataReader.getJsonFile(fileName);

        stepsData.validatableResponse = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec())
                    .baseUri(baseUri)
                    .body(jsonInvalidUser)
                    .contentType(ContentType.JSON)
                .when()
                    .post(pathLogin)
                .then();
    }

    @When("{string} starts to login with no credentials")
    public void UserStartsToLoginWithNoCredentials(String username) {
        stepsData.validatableResponse = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec())
                    .baseUri(baseUri)
                    .contentType(ContentType.JSON)
                .when()
                    .post(pathLogin)
                .then();
    }

    @And("{string} is created")
    public void UserIsCreated(String username) {
         this.stepsData.validatableResponse = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec())
                    .baseUri(baseUri)
                    .body(stepsData.user)
                    .contentType(ContentType.JSON)
                .when()
                    .post(pathUsers)
                .then();

    }

    @And("Json in response body matches createdUser.json")
    public void JsonInResponseBodyMatchesCreatedUserJson(){
        this.stepsData.validatableResponse.assertThat()
                .body(matchesJsonSchemaInClasspath("createdUser.json"));
    }

    @And("Response object is properly validated as an user object of an user {string}")
    public void ResponseObjectIsProperlyValidatedAsAnUserObject(String username) {
        Response response = this.stepsData.validatableResponse.extract().response();

        ObjectMapper objectMapper = new ObjectMapper();
        UserCreated userCreated = objectMapper.convertValue(response.jsonPath().get("user"),UserCreated.class);
        log.debug("userCreated: "+userCreated.toString());

        Assertions.assertEquals(stepsData.user.getFirstName(), userCreated.getFirstName());
        Assertions.assertEquals(stepsData.user.getLastName(), userCreated.getLastName());
        Assertions.assertEquals(stepsData.user.getUsername(), userCreated.getUsername());
        Assertions.assertTrue(userCreated.getPassword() != null);
        Assertions.assertTrue(UUID.fromString(userCreated.getUuid()) != null);

        stepsData.UsersIdMap.put(username, userCreated);
    }

    @And("Correct user object is received")
    public void CorrectUserObject() {
        Response response = stepsData.validatableResponse.assertThat()
                .body(matchesJsonSchemaInClasspath("loggedUser.json"))
                .extract()
                .response();

        ObjectMapper objectMapper = new ObjectMapper();
        UserCreated userCreated = objectMapper.convertValue(response.jsonPath().get("user"),UserCreated.class); // ?
        log.debug("userCreated: "+userCreated.toString());

        Assertions.assertEquals(stepsData.user.getFirstName(), userCreated.getFirstName());
        Assertions.assertEquals(stepsData.user.getLastName(), userCreated.getLastName());
        Assertions.assertEquals(stepsData.user.getUsername(), userCreated.getUsername());
        Assertions.assertTrue(userCreated.getPassword() != null); //
        Assertions.assertTrue(UUID.fromString(userCreated.getUuid()) != null);
    }

}
