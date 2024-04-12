package org.cypress.example.bdd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.cypress.example.BaseTest;
import org.cypress.example.model.TransactionGet;
import org.cypress.example.model.UserGet;
import org.dataProviders.JsonDataReader;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

public class RegistrationBddClass extends BaseTest{

    static final Logger log = getLogger(lookup().lookupClass());
    StepsData stepsData;

    public RegistrationBddClass(StepsData stepsData) {
        this.stepsData = stepsData;
    }

    @When("{string} is created with not all data provided in request based on {string}")
    public void UserIsCreatedWithNotAllDataProvidedInRequestBasedOnFile(String username, String fileName) {
        String jsonInvalidUser = JsonDataReader.getJsonFile(fileName);

        stepsData.validatableResponse = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec())
                    .baseUri(baseUri)
                    .body(jsonInvalidUser)
                    .contentType(ContentType.JSON)
                .when()
                    .post(pathUsers)
                .then();
    }

    @And("Response object is validated with a file {string}")
    public void ResponseObjectIsNotValidatedWithAFileCreatedUserJson(String fileName) {
//        this.stepsData.validatableResponse.assertThat()
//                .body(matchesJsonSchemaInClasspath("testDataResources/RegistrationInvalid/schemas/EmptyFirstName.json"));


        this.stepsData.validatableResponse.assertThat()
                .body(matchesJsonSchemaInClasspath(fileName));
    }

    @And("Get request for users list is sent")
    public void GetRequestForUsersListIsSent() {
        this.stepsData.validatableResponse = RestAssured
                .given(BaseTest.SpecBuilder.getRequestSpec(this.stepsData.cookieValue))
                    .baseUri(baseUri)
                    .contentType(ContentType.JSON)
                .when()
                    .get(pathUsers)
                .then();
    }

    @And("Response contains {string}")
    public void ResponseContainsUsername(String username) {
        Response response = this.stepsData.validatableResponse.extract().response();

        ObjectMapper objectMapper = new ObjectMapper();

        List<UserGet> userGetList = objectMapper.convertValue(stepsData.validatableResponse.extract().
                response().jsonPath().get("results"),  new TypeReference<List<UserGet>>(){});

        UserGet userGet = userGetList.stream()
                .filter(item -> item.username.equals(username))
                .findFirst().get();

        Assertions.assertTrue(userGet != null);
    }

    @And("Response does not contain object for {string}")
    public void ResponseDoesNotContainUsername(String username) {
        Response response = this.stepsData.validatableResponse.extract().response();

        ObjectMapper objectMapper = new ObjectMapper();
        List<UserGet> userGetList = objectMapper.convertValue(response.jsonPath().get("results"), new TypeReference<List<UserGet>>() { });

        UserGet userGet = userGetList.stream()
                .filter(item -> item.username.equals(username))
                .findFirst().orElse(null);

        Assertions.assertTrue(userGet == null);

    }
}
