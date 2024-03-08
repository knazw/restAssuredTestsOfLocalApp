package org.cypress.example;

import org.cypress.example.models.CreateTransaction;

public class BaseTransactionTest extends BaseTest{
    protected CreateTransaction createTransaction;

    protected void createTransactionData() {
        createTransaction = new CreateTransaction();
        createTransaction.transactionType = "payment";
        createTransaction.amount = "100";
        createTransaction.description = "note 1";
        createTransaction.senderId = userId0;
        createTransaction.receiverId = userId1;

    }
}
