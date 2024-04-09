package org.cypress.example.bdd;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.cypress.example.BaseTransactionTest;
import org.cypress.example.models.CreateTransaction;
import org.cypress.example.models.LikeTransaction;
import org.cypress.example.models.UpdateNotification;
import org.dataProviders.JsonDataReader;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;

public class NotificationBddClass extends BaseTransactionTest {

    private StepsData stepsData;

    @After
    public void afterEach() {
        clearData();
    }

    @Before
    public void beforeEach() {
        clearData();
    }


    public NotificationBddClass(StepsData stepsData) {
        this.stepsData = stepsData;
    }

    // And I create a "<transaction>" transaction from user "<username>" to "<username1>" with <amount> and description "<description>"
    @And("{string} creates a {string} transaction from user {string} to {string} with {int} and description {string}")
    public void IHaveTheFollowingCredentials(String usernameArg, String transactionType, String username, String username1, int amount, String description) {
        JsonDataReader jsonDataReader = new JsonDataReader();
//        stepsData.user = jsonDataReader.getUserByUsername(username);
//        stepsData.user1 = jsonDataReader.getUserByUsername(username1);

        createTransaction = new CreateTransaction();
        createTransaction.senderId = stepsData.UsersIdMap.get(username).id;
        createTransaction.receiverId = stepsData.UsersIdMap.get(username1).id;
        createTransaction.amount = amount + "";
        createTransaction.description = description;
        createTransaction.transactionType = transactionType;

        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookieValue))
                    .body(createTransaction)
                .when()
                    .post(pathTransactions)
                .then();
    }

    @When("{string} likes this transaction")
    public void ILikeThisTransaction(String username) {
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

    @And("Response message contains OK")
    public void ResponseMessageContainsOK() {
        stepsData.validatableResponse.body(containsString("OK"));
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
        JsonPath jsonPathEvaluator =  stepsData.validatableResponse.extract().response().jsonPath();

        String likeUserId = jsonPathEvaluator.get("results[0].likes[0].userId");
        String likeId = jsonPathEvaluator.get("results[0].likes[0].id");

        Assertions.assertEquals(stepsData.UsersIdMap.get(username).id, likeUserId);
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

    @And("This is possible to obtain notificationId from get notification response")
    public void ThisIsPossibleToObtainNotificationIdFromGetNotificationResponse() {
        Response response = stepsData.validatableResponse.extract().response();
        stepsData.notificationId = response.jsonPath().get("results[0].id");
        Assertions.assertTrue(stepsData.notificationId != null);
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


}
