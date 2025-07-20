package org.cypress.example.bdd;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.cypress.example.utils.AllureLoggerUtils;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.hamcrest.Matchers.containsString;
import static org.slf4j.LoggerFactory.getLogger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonSteps {

    static final Logger log = getLogger(lookup().lookupClass());
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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

    @And("Cookie can be obtained from response header for {string}")
    public void CookieCanBeObtainedFromResponseHeaderForAnUser(String username) {
        Response response = stepsData.validatableResponse
                .extract()
                .response();

        String cookieValue = response.header("Set-Cookie");

        String[] cookieHeaderArray = cookieValue.split(";");
        cookieValue = cookieHeaderArray[0];

        stepsData.cookiesValues.put(username, cookieValue);
    }

    @Then("{int} response code is received")
    public void ResponseCodeIsReceived(int statusCode) {
        AllureLoggerUtils.logStepWithTimestamp(statusCode +" response code is received");
        stepsData.validatableResponse.statusCode(statusCode);
    }

    @And("Response message {string}")
    public void ResponseMessage(String string) {
        stepsData.validatableResponse.body(containsString(string));

        // Używanie timestamped step z attachmentem
        AllureLoggerUtils.createTimestampedStep(
                "Obtaining data from response",
                String.format("Response message: %s ", string)
        );
    }

    @And("Response message contains OK")
    public void ResponseMessageContainsOK() {
        stepsData.validatableResponse.body(containsString("OK"));
    }

    @And("Response message contains {string}")
    public void ResponseMessageContainsMessage(String message) {
        stepsData.validatableResponse.body(containsString(message));
    }

    @And("Response message contains Unathorized")
    public void ResponseMessageContainsUnathorized() {
        //And Response message contains OK
        stepsData.validatableResponse.body(containsString("Unauthorized"));
    }
}
