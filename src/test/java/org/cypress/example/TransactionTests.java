package org.cypress.example;

import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.cypress.example.models.AddTransactionComment;
import org.cypress.example.models.LikeTransaction;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;

public class TransactionTests extends BaseTransactionTest {

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
    public void createTransactionTest() {
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

        Response getTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getTransactionResponse.jsonPath();
        String getTransactionId = jsonPathEvaluator.get("results[0].id");
        int getTransactionAmount = jsonPathEvaluator.get("results[0].amount");
        String getTransactionReceiverName = jsonPathEvaluator.get("results[0].receiverName");
        String getTransactionSenderName = jsonPathEvaluator.get("results[0].senderName");
        String getTransactionReceiverId = jsonPathEvaluator.get("results[0].receiverId");
        String getTransactionSenderId = jsonPathEvaluator.get("results[0].senderId");
        String getTransactionDescription = jsonPathEvaluator.get("results[0].description");
        String getTransactionStatus = jsonPathEvaluator.get("results[0].status");
        int getTransactionPage = jsonPathEvaluator.get("pageData.page");
        boolean getTransactionHasNextPages = jsonPathEvaluator.get("pageData.hasNextPages");
        int getTransactionTotalPages = jsonPathEvaluator.get("pageData.totalPages");

        List<Map<String, Object>> getTransactionLikes = jsonPathEvaluator.get("results[0].likes");
        List<Map<String, Object>> getTransactionComments = jsonPathEvaluator.get("results[0].comments");

        Assertions.assertEquals(postTransactionId, getTransactionId);
        Assertions.assertEquals(Integer.parseInt(createTransaction.amount) * 100, getTransactionAmount);
        Assertions.assertEquals(createTransaction.receiverId, getTransactionReceiverId);
        Assertions.assertEquals(loggedUserId, getTransactionSenderId);
        Assertions.assertEquals(user.getFirstName() + " "+ user.getLastName(),getTransactionSenderName);
        Assertions.assertEquals(user1.getFirstName() + " "+ user1.getLastName(),getTransactionReceiverName);

        Assertions.assertEquals(createTransaction.description, getTransactionDescription);
        Assertions.assertEquals("complete", getTransactionStatus);

        Assertions.assertEquals(1, getTransactionPage);
        Assertions.assertFalse(getTransactionHasNextPages);
        Assertions.assertEquals(1, getTransactionTotalPages);

        Assertions.assertEquals(0, getTransactionLikes.size());
        Assertions.assertEquals(0, getTransactionComments.size());
    }

    @Test
    public void createAndLikeTransaction() {
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

        Response getTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getTransactionResponse.jsonPath();
        String getTransactionId = jsonPathEvaluator.get("results[0].id");
        int getTransactionAmount = jsonPathEvaluator.get("results[0].amount");
        String getTransactionReceiverName = jsonPathEvaluator.get("results[0].receiverName");
        String getTransactionSenderName = jsonPathEvaluator.get("results[0].senderName");
        String getTransactionReceiverId = jsonPathEvaluator.get("results[0].receiverId");
        String getTransactionSenderId = jsonPathEvaluator.get("results[0].senderId");
        String getTransactionDescription = jsonPathEvaluator.get("results[0].description");
        String getTransactionStatus = jsonPathEvaluator.get("results[0].status");
        int getTransactionPage = jsonPathEvaluator.get("pageData.page");
        boolean getTransactionHasNextPages = jsonPathEvaluator.get("pageData.hasNextPages");
        int getTransactionTotalPages = jsonPathEvaluator.get("pageData.totalPages");

        List<Map<String, Object>> getTransactionLikes = jsonPathEvaluator.get("results[0].likes");
        List<Map<String, Object>> getTransactionComments = jsonPathEvaluator.get("results[0].comments");

        Assertions.assertEquals(postTransactionId, getTransactionId);
        Assertions.assertEquals(Integer.parseInt(createTransaction.amount) * 100, getTransactionAmount);
        Assertions.assertEquals(createTransaction.receiverId, getTransactionReceiverId);
        Assertions.assertEquals(loggedUserId, getTransactionSenderId);
        Assertions.assertEquals(user.getFirstName() + " "+ user.getLastName(),getTransactionSenderName);
        Assertions.assertEquals(user1.getFirstName() + " "+ user1.getLastName(),getTransactionReceiverName);

        Assertions.assertEquals(createTransaction.description, getTransactionDescription);
        Assertions.assertEquals("complete", getTransactionStatus);

        Assertions.assertEquals(1, getTransactionPage);
        Assertions.assertFalse(getTransactionHasNextPages);
        Assertions.assertEquals(1, getTransactionTotalPages);

        Assertions.assertEquals(0, getTransactionLikes.size());
        Assertions.assertEquals(0, getTransactionComments.size());

        LikeTransaction likeTransaction = new LikeTransaction();
        likeTransaction.transactionId = getTransactionId;


        Response postLikeTransactionResponse = RestAssured
                .given(SpecBuilder.getRequestSpec())
                .body(likeTransaction)
            .when()
                .post(pathLikesTransaction+likeTransaction.transactionId)
            .then()
                .statusCode(200)
                .body(containsString("OK"))
                .extract()
                .response();

        Response getTransactionAfterLikeResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getTransactionAfterLikeResponse.jsonPath();
        String likeTransactionUserId = jsonPathEvaluator.get("results[0].likes[0].userId");
        String likeTransactionTransactionId = jsonPathEvaluator.get("results[0].likes[0].transactionId");

        Assertions.assertEquals(loggedUserId, likeTransactionUserId);
        Assertions.assertEquals(likeTransaction.transactionId, likeTransactionTransactionId);
    }

    @Test
    public void createAndCommentTransaction() {
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

        Response getTransactionResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getTransactionResponse.jsonPath();
        String getTransactionId = jsonPathEvaluator.get("results[0].id");
        int getTransactionAmount = jsonPathEvaluator.get("results[0].amount");
        String getTransactionReceiverName = jsonPathEvaluator.get("results[0].receiverName");
        String getTransactionSenderName = jsonPathEvaluator.get("results[0].senderName");
        String getTransactionReceiverId = jsonPathEvaluator.get("results[0].receiverId");
        String getTransactionSenderId = jsonPathEvaluator.get("results[0].senderId");
        String getTransactionDescription = jsonPathEvaluator.get("results[0].description");
        String getTransactionStatus = jsonPathEvaluator.get("results[0].status");
        int getTransactionPage = jsonPathEvaluator.get("pageData.page");
        boolean getTransactionHasNextPages = jsonPathEvaluator.get("pageData.hasNextPages");
        int getTransactionTotalPages = jsonPathEvaluator.get("pageData.totalPages");

        List<Map<String, Object>> getTransactionLikes = jsonPathEvaluator.get("results[0].likes");
        List<Map<String, Object>> getTransactionComments = jsonPathEvaluator.get("results[0].comments");

        Assertions.assertEquals(postTransactionId, getTransactionId);
        Assertions.assertEquals(Integer.parseInt(createTransaction.amount) * 100, getTransactionAmount);
        Assertions.assertEquals(createTransaction.receiverId, getTransactionReceiverId);
        Assertions.assertEquals(loggedUserId, getTransactionSenderId);
        Assertions.assertEquals(user.getFirstName() + " "+ user.getLastName(),getTransactionSenderName);
        Assertions.assertEquals(user1.getFirstName() + " "+ user1.getLastName(),getTransactionReceiverName);

        Assertions.assertEquals(createTransaction.description, getTransactionDescription);
        Assertions.assertEquals("complete", getTransactionStatus);

        Assertions.assertEquals(1, getTransactionPage);
        Assertions.assertFalse(getTransactionHasNextPages);
        Assertions.assertEquals(1, getTransactionTotalPages);

        Assertions.assertEquals(0, getTransactionLikes.size());
        Assertions.assertEquals(0, getTransactionComments.size());

//        LikeTransaction likeTransaction = new LikeTransaction();
//        likeTransaction.transactionId = getTransactionId;
        AddTransactionComment addTransactionComment = new AddTransactionComment();
        addTransactionComment.transactionId = getTransactionId;
        addTransactionComment.content = "Comment for "+getTransactionId;

        Response postCommentTransactionResponse = RestAssured
                .given(SpecBuilder.getRequestSpec())
                .body(addTransactionComment)
            .when()
                .post(pathComments+getTransactionId)
            .then()
                .statusCode(200)
                .body(containsString("OK"))
                .extract()
                .response();


        Response getTransactionAfterCommentResponse = RestAssured.given(SpecBuilder.getRequestSpec())
            .when()
                .get(pathTransactions)
            .then()
                .statusCode(200)
                .extract()
                .response();

        jsonPathEvaluator = getTransactionAfterCommentResponse.jsonPath();
        String commentTransactionUserId = jsonPathEvaluator.get("results[0].comments[0].userId");
        String commentTransactionTransactionId = jsonPathEvaluator.get("results[0].comments[0].transactionId");

        Assertions.assertEquals(loggedUserId, commentTransactionUserId);
        Assertions.assertEquals(postTransactionId, commentTransactionTransactionId);
    }

}
