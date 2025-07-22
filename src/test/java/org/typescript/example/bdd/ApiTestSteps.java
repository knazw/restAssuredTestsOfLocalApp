package org.typescript.example.bdd;

//package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.cypress.example.utils.AllureLoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// WireMock imports
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

// Rest-Assured imports
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApiTestSteps {
    private static final Logger logger = LoggerFactory.getLogger(ApiTestSteps.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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

    @Given("mock API server is running")
    public void mockApiServerIsRunning() {
        AllureLoggerUtils.logStepWithTimestamp("Initializing mock API server");
        setupMockApiWithTimestamp();
    }

    @Given("I have configured mock endpoint for user {string}")
    public void iHaveConfiguredMockEndpointForUser(String userId) {
        AllureLoggerUtils.createTimestampedStep(
                "Configure mock endpoint",
                String.format("Setting up mock for user ID: %s", userId)
        );

        setupUserMockEndpoint(userId);
    }

    @When("I send GET request to {string}")
    public void iSendGetRequestTo(String endpoint) {
        AllureLoggerUtils.dynamicTimestampedStep("Sending GET request to: %s", endpoint);

        executeGetRequestWithTimestamp(endpoint);
    }

    @Then("I should receive status code {int}")
    public void iShouldReceiveStatusCode(int expectedStatusCode) {
        AllureLoggerUtils.dynamicTimestampedStep("Verifying status code: %d", expectedStatusCode);

        verifyStatusCodeWithTimestamp(expectedStatusCode);
    }

    @Then("response should contain user name {string}")
    public void responseShouldContainUserName(String expectedName) {
        AllureLoggerUtils.createTimestampedStep(
                "Verify response content",
                String.format("Expected user name: %s", expectedName)
        );

        verifyResponseContentWithTimestamp(expectedName);
    }

    @Step("Setup mock API server at {timestamp}")
    private void setupMockApiWithTimestamp() {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        logger.info("[{}] Setting up WireMock server on port {}", timestamp, WIREMOCK_PORT);

        // Verify WireMock server is running
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }

        // Add basic health check endpoint
        stubFor(get(urlEqualTo("/health"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"UP\",\"timestamp\":\"" + timestamp + "\"}")));

        String logContent = String.format(
                "Mock API Server Setup%n" +
                        "Timestamp: %s%n" +
                        "Port: %d%n" +
                        "Base URL: http://localhost:%d%n" +
                        "Health Check: /health",
                timestamp, WIREMOCK_PORT, WIREMOCK_PORT
        );

        Allure.addAttachment("WireMock Setup", "text/plain", logContent);
    }

    @Step("Setup user mock endpoint at {timestamp}")
    private void setupUserMockEndpoint(String userId) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        logger.info("[{}] Setting up mock endpoint for user: {}", timestamp, userId);

        // Create mock user data
        String userJson = String.format("""
            {
                "id": "%s",
                "name": "Test User %s",
                "email": "user%s@example.com",
                "createdAt": "%s"
            }
            """, userId, userId, userId, timestamp);

        // Setup WireMock stub
        stubFor(get(urlEqualTo("/users/" + userId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(userJson)));

        // Add stub for user not found scenario
        stubFor(get(urlMatching("/users/(?!(" + userId + ")$).*"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"User not found\"}")));

        String logContent = String.format(
                "Mock Endpoint Configuration%n" +
                        "Timestamp: %s%n" +
                        "Endpoint: /users/%s%n" +
                        "Response: %s",
                timestamp, userId, userJson
        );

        Allure.addAttachment("Mock Endpoint Setup", "text/plain", logContent);
    }

    @Step("Execute GET request at {timestamp}")
    private void executeGetRequestWithTimestamp(String endpoint) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        logger.info("[{}] Executing GET request to: {}", timestamp, endpoint);

        // Execute request using Rest-Assured
        given()
                .log().all()
                .header("Accept", "application/json")
                .header("X-Test-Timestamp", timestamp)
                .when()
                .get(endpoint)
                .then()
                .log().all();

        String logContent = String.format(
                "HTTP GET Request%n" +
                        "Timestamp: %s%n" +
                        "Endpoint: %s%n" +
                        "Base URL: %s%n" +
                        "Full URL: %s%s",
                timestamp, endpoint, RestAssured.baseURI + ":" + RestAssured.port,
                RestAssured.baseURI + ":" + RestAssured.port, endpoint
        );

        Allure.addAttachment("GET Request Details", "text/plain", logContent);
    }

    @Step("Verify status code at {timestamp}")
    private void verifyStatusCodeWithTimestamp(int expectedStatusCode) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        logger.info("[{}] Verifying status code: {}", timestamp, expectedStatusCode);

        given()
                .header("Accept", "application/json")
                .when()
                .get("/users/123") // Example endpoint
                .then()
                .statusCode(expectedStatusCode);

        AllureLoggerUtils.addTimestampedLog("Status code verification completed: " + expectedStatusCode);
    }

    @Step("Verify response content at {timestamp}")
    private void verifyResponseContentWithTimestamp(String expectedName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        logger.info("[{}] Verifying response contains name: {}", timestamp, expectedName);

        given()
                .header("Accept", "application/json")
                .when()
                .get("/users/123")
                .then()
                .statusCode(200)
                .body("name", equalTo(expectedName));

        AllureLoggerUtils.addTimestampedLog("Response content verification completed for: " + expectedName);
    }

    // Cleanup method to stop WireMock server
    public static void stopWireMockServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
