package org.typescript.example.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.containsString;

public class ApiChatGptSteps {
    /*
    private Response response;

    @Given("the WireMock server is running")
    public void startWireMock() {
        WireMockSetup.startIfNotRunning();
    }

    @When("I call GET /api/test")
    public void callGetEndpoint() {
        response = get("http://localhost:8089/api/test");
    }

    @Then("the response status should be {int}")
    public void checkStatus(int status) {
        response.then().statusCode(status);
    }

    @Then("the response body should contain {string}")
    public void checkBody(String text) {
        response.then().body(containsString(text));
    }
    */
}
