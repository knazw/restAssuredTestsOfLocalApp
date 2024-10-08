package org.cypress.example.bdd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.cypress.example.BaseTest;
import org.cypress.example.model.TransactionGet;
import org.cypress.example.model.CreateTransaction;
import org.cypress.example.model.LikeTransaction;
import org.cypress.example.model.UpdateNotification;
import org.dataProviders.JsonDataReader;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

public class NotificationBddClass extends BaseTest {

    static final Logger log = getLogger(lookup().lookupClass());
    private StepsData stepsData;

    public NotificationBddClass(StepsData stepsData) {
        this.stepsData = stepsData;
    }

    @And("{string} creates a {string} transaction from user {string} to {string} with {int} and description {string}")
    public void UserCreatesTransactionWithAmountAndDescription(String usernameArg, String transactionType, String username, String username1, int amount, String description) {
        JsonDataReader jsonDataReader = new JsonDataReader();

        CreateTransaction createTransaction = new CreateTransaction();
        createTransaction.senderId = stepsData.UsersIdMap.get(username).id;
        createTransaction.receiverId = stepsData.UsersIdMap.get(username1).id;
        createTransaction.amount = amount;
        createTransaction.description = description;
        createTransaction.transactionType = transactionType;

        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookieValue))
                    .body(createTransaction)
                .when()
                    .post(pathTransactions)
                .then();
    }

    @When("{string} creates a {string} transaction from user {string} to userId {string} with {int} and description {string}")
    public void UserCreatesTransactionWithAmountAndDescriptionToSpecificUserId(String usernameArg, String transactionType, String username, String userId, int amount, String description) {
        JsonDataReader jsonDataReader = new JsonDataReader();

        CreateTransaction createTransaction = new CreateTransaction();
        createTransaction.senderId = stepsData.UsersIdMap.get(username).id;
        createTransaction.receiverId = userId;
        createTransaction.amount = amount;
        createTransaction.description = description;
        createTransaction.transactionType = transactionType;

        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookieValue))
                    .body(createTransaction)
                .when()
                    .post(pathTransactions)
                .then();
    }

    @When("{string} likes this transaction")
    public void UserLikesTransaction(String username) {
        JsonPath jsonPathEvaluator = stepsData.validatableResponse.extract().jsonPath();
        String postTransactionId = jsonPathEvaluator.get("transaction.id");


        LikeTransaction likeTransaction = new LikeTransaction();
        likeTransaction.transactionId = postTransactionId;

        stepsData.likeTransaction = likeTransaction;

        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookieValue))
                    .body(likeTransaction)
                .when()
                    .post(pathLikesTransaction+postTransactionId)
                .then();


    }

    @When("{string} likes this not existing transaction")
    public void UserLikesThisNotExistingTransaction(String username) {
        String postTransactionId = "0";

        LikeTransaction likeTransaction = new LikeTransaction();
        likeTransaction.transactionId = postTransactionId;

        stepsData.likeTransaction = likeTransaction;

        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookieValue))
                .body(likeTransaction)
                .when()
                .post(pathLikesTransaction+postTransactionId)
                .then();
    }

    @And("It is possible to obtain this like by get transaction request")
    public void ItIspossibleToObtainThisLikeByGetRequest() {
        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookieValue))
                .when()
                    .get(pathTransactions)
                .then();

    }

    @And("This like response contains correct userId for {string}")
    public void ThisLikeResponseContainsCorrectUser(String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TransactionGet> transactionGetList = objectMapper.convertValue(stepsData.validatableResponse.extract().
                response().jsonPath().get("results"),  new TypeReference<List<TransactionGet>>(){});
        TransactionGet transactionGet = transactionGetList.stream()
                .filter(transactionGetItem -> transactionGetItem.id.equals(this.stepsData.transactionCreated.id))
                .findFirst().get();
        String likeUserId = transactionGet.likes.get(0).userId;

        Assertions.assertEquals(stepsData.UsersIdMap.get(username).id, likeUserId);
    }

    @And("It is not possible to find this like")
    public void ItIsNotPossibleToFindThisLike() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TransactionGet> transactionGetList = objectMapper.convertValue(stepsData.validatableResponse.extract().
                response().jsonPath().get("results"),  new TypeReference<List<TransactionGet>>(){});
        TransactionGet transactionGet = transactionGetList.stream()
                .filter(transactionGetItem -> transactionGetItem.id.equals(this.stepsData.transactionCreated.id))
                .findFirst().orElse(null);

        Assertions.assertNull(transactionGet);
    }

    @And("This like response contains correct transactionId")
    public void ThisLikeResponseContainsCorrectTransactionId() {
        JsonPath jsonPathEvaluator =  stepsData.validatableResponse.extract().response().jsonPath();
        String likeTransactionId = jsonPathEvaluator.get("results[0].likes[0].transactionId");

        Assertions.assertEquals(stepsData.likeTransaction.transactionId, likeTransactionId);
    }

    @And("It is possible to send get notification request")
    public void ThisIsPossibleToSendGetNotificationRequest() {
        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookieValue))
                .when()
                    .get(pathNotifications)
                .then();


        // todo get notification List<T> instead of json
    }

    @And("It is possible to obtain notificationId from get notification response")
    public void ThisIsPossibleToObtainNotificationIdFromGetNotificationResponse() {
        Response response = stepsData.validatableResponse.extract().response();
        stepsData.notificationId = response.jsonPath().get("results[0].id");
        Assertions.assertTrue(stepsData.notificationId != null);
    }

    @And("It is not possible to obtain notificationId from get notification response")
    public void ThisIsNotPossibleToObtainNotificationIdFromGetNotificationResponse() {
        Response response = stepsData.validatableResponse.extract().response();
        stepsData.notificationId = response.jsonPath().get("results[0].id");
        Assertions.assertNull(stepsData.notificationId);
    }

    @And("Notification is read")
    public void ThisIsPossibleToReadThisNotification() {
        UpdateNotification updateNotification = new UpdateNotification();
        updateNotification.isRead = true;
        updateNotification.id = stepsData.notificationId;

        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookieValue))
                    .body(updateNotification)
                .when()
                    .patch(pathNotifications+stepsData.notificationId)
                .then();
    }

    @And("Notification is read by {string}")
    public void ThisIsPossibleToReadThisNotificationByAnUser(String username) {
        UpdateNotification updateNotification = new UpdateNotification();
        updateNotification.isRead = true;
        updateNotification.id = stepsData.notificationId;

        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookiesValues.get(username)))
                .body(updateNotification)
                .when()
                .patch(pathNotifications+stepsData.notificationId)
                .then();
    }

    @And("{int} objects are returned after get notification request")
    public void IntObjectsAreReturnedAfterGetNotificationRequest(int quantity) {
        JsonPath jsonPathEvaluator = stepsData.validatableResponse.extract().jsonPath();
        List<Map<String, Object>> results = jsonPathEvaluator.get("results");

        Assertions.assertEquals(quantity, results.size());
    }

    @And("{int} is choosen")
    public void IntNotificationNrIsChoosen(int notificationNr) {
        stepsData.notificationId = stepsData.validatableResponse.extract().jsonPath().get("results["+notificationNr+"].id");
    }

    @And("Not existing notification is choosen")
    public void NotExistingNotificationIdIsChoosen() {
        stepsData.notificationId = "0";
    }

    @And("Error contains {string}")
    public void ErrorContains(String msg) {
        String body = stepsData.validatableResponse.extract().body().asPrettyString();

        Assertions.assertTrue(body.contains(msg));
    }

}
