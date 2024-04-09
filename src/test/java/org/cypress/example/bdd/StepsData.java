package org.cypress.example.bdd;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.cypress.example.model.TransactionCreated;
import org.cypress.example.model.TransactionGet;
import org.cypress.example.model.User;
import org.cypress.example.model.UserCreated;
import org.cypress.example.models.LikeTransaction;

import java.util.HashMap;

public class StepsData {
    public User user;
    public LikeTransaction likeTransaction;
    public TransactionCreated transactionCreated;
    public TransactionGet transactionGet;
    public HashMap<String, UserCreated> UsersIdMap = new HashMap<>();
    public ValidatableResponse validatableResponse;
    public Response response;
    public String cookieValue;
    public String notificationId;

}
