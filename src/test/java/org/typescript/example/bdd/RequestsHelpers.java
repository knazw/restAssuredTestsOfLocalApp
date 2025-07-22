package org.typescript.example.bdd;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class RequestsHelpers {

    public ValidatableResponse getRequestSpec(RequestSpecification requestSpec, String method, String endpoint) {
        switch (method) {
            case "GET":
                return requestSpec
                            .get(endpoint)
                        .then()
                            .log().all();
            case "POST":
                return requestSpec
                            .post(endpoint)
                        .then()
                            .log().all();
            case "PUT":
                return requestSpec
                            .put(endpoint)
                        .then()
                            .log().all();
            case "DELETE":
                return requestSpec
                            .delete(endpoint)
                        .then()
                            .log().all();
            case "PATCH":
                return requestSpec
                            .patch(endpoint)
                        .then()
                            .log().all();
            default:
                return requestSpec
                            .get(endpoint)
                        .then()
                            .log().all();
        }
    }
}
