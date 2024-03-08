package org.cypress.example;

import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.cypress.example.models.LikeTransaction;
import org.cypress.example.models.UpdateNotification;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;

public class NotificationTests extends BaseTransactionTest{
    protected LikeTransaction likeTransaction;

    @BeforeAll
    public static void beforeClass() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter());

        createUsers();
    }

    @BeforeEach
    public void beforeEach() {

        addUserAndLogin();
    }

    @AfterEach
    public void afterEach() {
        clearData();
    }

    @Test
    public void readNotificationTest() {
        // GIVEN
        createTransactionData();

        Response createTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(createTransaction)
            .when()
                .post(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPathEvaluator = createTransactionResponse.jsonPath();
        String postTransactionId = jsonPathEvaluator.get("transaction.id");

        createLikeTransactionData(postTransactionId);

        // WHEN
        Response likeTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(likeTransaction)
            .when()
                .post(pathLikesTransaction+postTransactionId)
            .then()
                .statusCode(200)
                .body(containsString("OK"))
                .extract()
                .response();

        // THEN
        /* getTransactions to have:
            - likeId v
            - userId - same like loggedInUserId v
            - transactionId v

            compare with:
            - nothing v
            - loggedInUserId v
            - transactionId v


           getNotification to have:
            - userFullName v
            - notificationId v
            - userId v
            - transactionId v
            - likeId v
            - isRead v
         */

        Response getTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getTransactionResponse.jsonPath();
        String likeTransactionId = jsonPathEvaluator.get("results[0].likes[0].transactionId");
        String likeUserId = jsonPathEvaluator.get("results[0].likes[0].userId");
        String likeId = jsonPathEvaluator.get("results[0].likes[0].id");

        Assertions.assertEquals(postTransactionId, likeTransactionId);
        Assertions.assertEquals(loggedUserId, likeUserId);

        Response getNotificationResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathNotifications)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getNotificationResponse.jsonPath();
        String notificationUserFullName = jsonPathEvaluator.get("results[0].userFullName");
        String notificationId = jsonPathEvaluator.get("results[0].id");
        String notificationUserId = jsonPathEvaluator.get("results[0].userId");
        String notificationTransactionId = jsonPathEvaluator.get("results[0].transactionId");
        String notificationLikeId = jsonPathEvaluator.get("results[0].likeId");
        boolean notificationIsRead = jsonPathEvaluator.get("results[0].isRead");

        UpdateNotification updateNotification = createPatchNotificationData(notificationId);

        Response patchNotificationResponse = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(updateNotification)
            .when()
                .patch(pathNotifications+notificationId)
            .then()
                .statusCode(204)
                .extract()
                .response();

        getNotificationResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathNotifications)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getNotificationResponse.jsonPath();
        ArrayList results = jsonPathEvaluator.get("results");

        Assertions.assertEquals(0, results.size());
    }

    @Test
    public void readOnlyOneNotificationTest() {
        // GIVEN

        // create first transaction:
        createTransactionData();

        Response createTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(createTransaction)
            .when()
                .post(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPathEvaluator = createTransactionResponse.jsonPath();
        String postTransactionId0 = jsonPathEvaluator.get("transaction.id");

        createLikeTransactionData(postTransactionId0);

        // like first transaction
        Response likeTransactionResponse0 = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(likeTransaction)
            .when()
                .post(pathLikesTransaction+postTransactionId0)
            .then()
                .statusCode(200)
                .body(containsString("OK"))
                .extract()
                .response();


        // create next transaction 2:

        createTransaction.amount = "200";
        createTransaction.description = "Note 2";

        createTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(createTransaction)
            .when()
                .post(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = createTransactionResponse.jsonPath();
        String postTransactionId1 = jsonPathEvaluator.get("transaction.id");

        createLikeTransactionData(postTransactionId1);

        // like next transaction 2:
        Response likeTransactionResponse1 = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(likeTransaction)
            .when()
                .post(pathLikesTransaction+postTransactionId1)
            .then()
                .statusCode(200)
                .body(containsString("OK"))
                .extract()
                .response();

        // create third transaction:
        createTransaction.amount = "300";
        createTransaction.description = "Note 3";

        Response createTransactionResponse2 = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(createTransaction)
            .when()
                .post(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = createTransactionResponse2.jsonPath();
        String postTransactionId2 = jsonPathEvaluator.get("transaction.id");

        // like third transaction
        createLikeTransactionData(postTransactionId2);

        Response likeTransactionResponse2 = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(likeTransaction)
            .when()
                .post(pathLikesTransaction+postTransactionId2)
            .then()
                .statusCode(200)
                .body(containsString("OK"))
                .extract()
                .response();

        // getNotifications -> notificationId

        Response getNotificationResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathNotifications)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getNotificationResponse.jsonPath();
        String deletedNotificationId = jsonPathEvaluator.get("results[2].id");
        String shouldStayNotificationId0 = jsonPathEvaluator.get("results[0].id");
        String shouldStayNotificationId1 = jsonPathEvaluator.get("results[1].id");


        // WHEN patch notification with notificationId
        UpdateNotification updateNotification = createPatchNotificationData(deletedNotificationId);

        Response patchNotificationResponse = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(updateNotification)
            .when()
                .patch(pathNotifications+deletedNotificationId)
            .then()
                .statusCode(204)
                .extract()
                .response();



        // THEN only 2 notifications left
        getNotificationResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathNotifications)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getNotificationResponse.jsonPath();
        String presentNotificationId0 = jsonPathEvaluator.get("results[0].id");
        String presentNotificationId1 = jsonPathEvaluator.get("results[1].id");
        ArrayList presentNotificationList = jsonPathEvaluator.get("results");


        Assertions.assertEquals(shouldStayNotificationId0, presentNotificationId0);
        Assertions.assertEquals(shouldStayNotificationId1, presentNotificationId1);
        Assertions.assertEquals(2, presentNotificationList.size());


        // getTransactions -> 3 likes
        Response getTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getTransactionResponse.jsonPath();
        String likeTransactionId0 = jsonPathEvaluator.get("results[0].likes[0].transactionId");
        String likeTransactionId1 = jsonPathEvaluator.get("results[1].likes[0].transactionId");
        String likeTransactionId2 = jsonPathEvaluator.get("results[2].likes[0].transactionId");

        Assertions.assertEquals(postTransactionId0, likeTransactionId2);
        Assertions.assertEquals(postTransactionId1, likeTransactionId1);
        Assertions.assertEquals(postTransactionId2, likeTransactionId0);
    }

    @Test
    public void readNotificationTestDeleteThisSpecBuilder() {
        // GIVEN
        createTransactionData();

        Response createTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(createTransaction)
            .when()
                .post(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPathEvaluator = createTransactionResponse.jsonPath();
        String postTransactionId = jsonPathEvaluator.get("transaction.id");

        createLikeTransactionData(postTransactionId);

        // WHEN
        Response likeTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(likeTransaction)
            .when()
                .post(pathLikesTransaction+postTransactionId)
            .then()
                .statusCode(200)
                .body(containsString("OK"))
                .extract()
                .response();

        // THEN
        /* getTransactions to have:
            - likeId v
            - userId - same like loggedInUserId v
            - transactionId v

            compare with:
            - nothing v
            - loggedInUserId v
            - transactionId v


           getNotification to have:
            - userFullName v
            - notificationId v
            - userId v
            - transactionId v
            - likeId v
            - isRead v
         */

        Response getTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getTransactionResponse.jsonPath();
        String likeTransactionId = jsonPathEvaluator.get("results[0].likes[0].transactionId");
        String likeUserId = jsonPathEvaluator.get("results[0].likes[0].userId");
        String likeId = jsonPathEvaluator.get("results[0].likes[0].id");

        Assertions.assertEquals(postTransactionId, likeTransactionId);
        Assertions.assertEquals(loggedUserId, likeUserId);

        Response getNotificationResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathNotifications)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getNotificationResponse.jsonPath();
        String notificationUserFullName = jsonPathEvaluator.get("results[0].userFullName");
        String notificationId = jsonPathEvaluator.get("results[0].id");
        String notificationUserId = jsonPathEvaluator.get("results[0].userId");
        String notificationTransactionId = jsonPathEvaluator.get("results[0].transactionId");
        String notificationLikeId = jsonPathEvaluator.get("results[0].likeId");
        boolean notificationIsRead = jsonPathEvaluator.get("results[0].isRead");

        UpdateNotification updateNotification = createPatchNotificationData(notificationId);

        Response patchNotificationResponse = RestAssured.given(SpecBuilder.getRequestSpec())
                .body(updateNotification)
            .when()
                .patch(pathNotifications+notificationId)
            .then()
                .statusCode(204)
                .extract()
                .response();

        getNotificationResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathNotifications)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getNotificationResponse.jsonPath();
        ArrayList results = jsonPathEvaluator.get("results");

        Assertions.assertEquals(0, results.size());
    }


    protected LikeTransaction createLikeTransactionData(String transactionId) {
        likeTransaction = new LikeTransaction();
        likeTransaction.transactionId = transactionId;

        return likeTransaction;
    }

    protected UpdateNotification createPatchNotificationData(String notificationId) {
        UpdateNotification updateNotification = new UpdateNotification();
        updateNotification.isRead = true;
        updateNotification.id = notificationId;

        return updateNotification;
    }
}
