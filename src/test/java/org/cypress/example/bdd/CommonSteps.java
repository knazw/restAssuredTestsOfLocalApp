package org.cypress.example.bdd;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.hamcrest.Matchers.containsString;
import static org.slf4j.LoggerFactory.getLogger;

public class CommonSteps {

    static final Logger log = getLogger(lookup().lookupClass());

    private StepsData stepsData;

    public CommonSteps(StepsData stepsData) {
        this.stepsData = stepsData;
    }


    @And("Cookie can be obtained from response header")
    public void CookieCanBeObtainedFromResponseHeader() {
        Response response = stepsData.validatableResponse
                .extract()
                .response();

        String cookieValue = response.header("Set-Cookie");

        String[] cookieHeaderArray = cookieValue.split(";");
        cookieValue = cookieHeaderArray[0];

        stepsData.cookieValue = cookieValue;
    }

    @Then("{int} response code is received")
    public void ResponseCodeIsReceived(int statusCode) {
        stepsData.validatableResponse.statusCode(statusCode);
    }

    @And("Response message {string}")
    public void ResponseMessage(String string) {
        stepsData.validatableResponse.body(containsString(string));
    }

    @And("Response message contains OK")
    public void ResponseMessageContainsOK() {
        stepsData.validatableResponse.body(containsString("OK"));
    }

    @And("Response message contains Unathorized")
    public void ResponseMessageContainsUnathorized() {
        //And Response message contains OK
        stepsData.validatableResponse.body(containsString("Unauthorized"));
    }
}
