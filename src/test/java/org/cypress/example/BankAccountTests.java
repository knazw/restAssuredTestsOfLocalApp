package org.cypress.example;

import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Header;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class BankAccountTests extends BaseTransactionTest{


    @BeforeEach
    public void beforeEach() {
        addUserAndLogin();
    }

    @BeforeAll
    public static void beforeClass() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter());

        createUsers();
    }

    @Test
    public void AddBankAccount() {
        String body = "{\n" +
                "    \"operationName\": \"CreateBankAccount\",\n" +
                "    \"query\": \"\\n  mutation CreateBankAccount($bankName: String!, $accountNumber: String!, $routingNumber: String!) {\\n    createBankAccount(\\n      bankName: $bankName\\n      accountNumber: $accountNumber\\n      routingNumber: $routingNumber\\n    ) {\\n      id\\n      uuid\\n      userId\\n      bankName\\n      accountNumber\\n      routingNumber\\n      isDeleted\\n      createdAt\\n    }\\n  }\\n\",\n" +
                "    \"variables\": {\n" +
                "        \"userId\": \""+userId0 +"\",\n" +
                "        \"bankName\": \"PKOBP\",\n" +
                "        \"accountNumber\": \"4356565446\",\n" +
                "        \"routingNumber\": \"968657543\"\n" +
                "    }\n" +
                "}\n";

        RestAssured.
                given(SpecBuilder.getRequestSpec())
                .body(body)
                .post("/graphql");

    }

    private static String graphqlToJson(String payload)
    {
        JSONObject json = new JSONObject();
        json.put("query",payload);
        return  json.toString();
    }

    @Test
    public void testGraphql() {
        RestAssured.baseURI = "https://www.predic8.de/fruit-shop-graphql?";
        String query = "query{" +
                "        products(id: \"7\") {" +
                "        name" +
                "                price" +
                "        category {" +
                "            name" +
                "        }" +
                "        vendor {" +
                "            name" +
                "                    id" +
                "        }" +
                "    }" +
                "    }";
        String jsonString = graphqlToJson(query);

        given()
                .log()
                .all()
                .contentType("application/json")
                .body(jsonString)
            .when()
                .log()
                .all()
                .post()
            .then()
                .log()
                .all()
                .assertThat()
                .statusLine("HTTP/1.1 200 OK");
    }

    @Test
    public void testGraphQL() throws MalformedURLException {
        String actual = RestAssured.
                given()
                .header(new Header("Content-type", "application/json"))
                .body("{\"query\":\"{\\n Country(id: \\\"us\\\") {\\n name\\n situation\\n }\\n}\\n \"}")
                .post(new URL("https://portal.ehri-project.eu/api/graphql"))
                .jsonPath().getString("data.Country.name");
        Assertions.assertEquals(actual, "United Sttates");
    }
}
