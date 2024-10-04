package org.cypress.example.bdd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.SerializationUtils;
import org.cypress.example.BaseTest;
import org.cypress.example.model.CreateTransaction;
import org.cypress.example.model.TransactionCreated;
import org.cypress.example.model.TransactionGet;
import org.dataProviders.JsonDataReader;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.util.List;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

public class TransactionBddClass extends BaseTest {
    static final Logger log = getLogger(lookup().lookupClass());
    private StepsData stepsData;

    public TransactionBddClass(StepsData stepsData) {
        this.stepsData = stepsData;
    }

    @And("Transaction object is obtained from response")
    public void TransactionObjectIsObtainedFromResponse() {
        Response response = this.stepsData.validatableResponse.extract().response();

        ObjectMapper objectMapper = new ObjectMapper();
        this.stepsData.transactionCreated = objectMapper.convertValue(response.jsonPath().get("transaction"), TransactionCreated.class);
    }

    @And("Not existing transaction object is stored")
    public void NotExistingTransactionObjectIsStored() {
        this.stepsData.transactionCreated = new TransactionCreated(null, null, 0, null, null, null, null, null, null,null);
    }

    @And("Correct transaction data are present in this object: {string}, {string}, {string}, {int}, {string}, {string} and {string}")
    public void CorrectTransactionDataArePresentInThisObject(String username,String username1,String transaction,int amount,String description, String status, String requestStatus) {
        String userId = this.stepsData.UsersIdMap.get(username).id;
        String userId1 = this.stepsData.UsersIdMap.get(username1).id;
        Assertions.assertEquals(userId, this.stepsData.transactionCreated.senderId);
//        Assertions.assertEquals(userId1, this.stepsData.transactionCreated.receiverId);
        Assertions.assertEquals(status, this.stepsData.transactionCreated.status);  // complete / pending
        if(requestStatus.isEmpty()) {
            Assertions.assertEquals(null, this.stepsData.transactionCreated.requestStatus);  // complete / pending
        }
        else {
            Assertions.assertEquals(requestStatus, this.stepsData.transactionCreated.requestStatus);  // complete / pending
        }
        Assertions.assertEquals(amount * 100, this.stepsData.transactionCreated.amount);
        Assertions.assertEquals(description, this.stepsData.transactionCreated.description);
        Assertions.assertTrue(!this.stepsData.transactionCreated.createdAt.isEmpty());
        Assertions.assertTrue(!this.stepsData.transactionCreated.modifiedAt.isEmpty());
        Assertions.assertEquals(this.stepsData.transactionCreated.createdAt, this.stepsData.transactionCreated.modifiedAt);
    }

    @And("TransactionId of this transaction is saved for later use")
    public void TransactionIdOfThisTransactionIsSavedForLaterUse() {

    }

    @And("It is possible to obtain transactions list by get transaction request")
    public void ItIsPossibleToObtainThisTransactionByGetTransactionRequest() {
        this.stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(this.stepsData.cookieValue))
                .when()
                    .get(pathTransactions)
                .then();

    }

    @And("It is possible to obtain transaction from transactions list")
    public void ItIsPossibleToObtainTransactionFromTransactionsList() {
        Response response = this.stepsData.validatableResponse.extract().response();

        ObjectMapper objectMapper = new ObjectMapper();
        List<TransactionGet> transactionGetList = objectMapper.convertValue(response.jsonPath().get("results"), new TypeReference<List<TransactionGet>>(){});

        this.stepsData.transactionGet = transactionGetList.stream()
                .filter(item -> item.id.equals(this.stepsData.transactionCreated.id))
                .findFirst().get();
    }

    @And("It is possible to compare obtained transaction with data: {string}, {string}, {string}, {int}, {string}, {string} and {string}")
    public void ItIsPossibleToCompareOtainedTransactionWithData(String username,String username1,String transaction,int amount,String description, String status, String requestStatus){
        String userId = this.stepsData.UsersIdMap.get(username).id;
        String userId1 = this.stepsData.UsersIdMap.get(username1).id;

        String user = this.stepsData.UsersIdMap.get(username).firstName + " " + this.stepsData.UsersIdMap.get(username).lastName;
        String user1 = this.stepsData.UsersIdMap.get(username1).firstName + " " + this.stepsData.UsersIdMap.get(username1).lastName;

        Assertions.assertEquals(userId, this.stepsData.transactionGet.senderId);
        Assertions.assertEquals(userId1, this.stepsData.transactionGet.receiverId);
        Assertions.assertEquals(amount * 100, this.stepsData.transactionGet.amount);
        Assertions.assertEquals(description, this.stepsData.transactionGet.description);

        Assertions.assertEquals(user, this.stepsData.transactionGet.senderName);
        Assertions.assertEquals(user1, this.stepsData.transactionGet.receiverName);
        Assertions.assertEquals(status, this.stepsData.transactionGet.status);
        if(requestStatus.isEmpty()) {
            Assertions.assertEquals(null, this.stepsData.transactionGet.requestStatus);
        }
        else {
            Assertions.assertEquals(requestStatus, this.stepsData.transactionGet.requestStatus);
        }
    }

    @When("User {string} accepts transaction")
    public void UserAcceptsRequestTransaction(String username) {
        TransactionGet transactionPatch = (TransactionGet) SerializationUtils.clone(this.stepsData.transactionGet);

        JsonDataReader jsonDataReader = new JsonDataReader();

        transactionPatch.status = "complete";
        transactionPatch.requestStatus = "accepted";

        stepsData.validatableResponse = RestAssured.given(SpecBuilder.getRequestSpec(stepsData.cookieValue))
                    .body(transactionPatch)
                .when()
                    .patch(pathTransactions + transactionPatch.id)
                .then();
    }



}
