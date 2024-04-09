package org.cypress.example.model;

public class TransactionCreated {
    public String id;
    public String uuid;
    public int amount;
    public String description;
    public String receiverId;
    public String senderId;
    public String status;
    public String createdAt;
    public String modifiedAt;

    public TransactionCreated() {

    }
    public TransactionCreated(String id, String uuid, int amount, String description, String receiverId, String senderId, String status, String createdAt, String modifiedAt) {
        this.id = id;
        this.uuid = uuid;
        this.amount = amount;
        this.description = description;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}

/*
{
    "transaction": {
        "id": "32HQirXY4",
        "uuid": "59dfb412-60b0-4e0a-be1c-4af52e2dd5b7",
        "amount": 10000,
        "description": "note 1",
        "receiverId": "_FuMZ93ix",
        "senderId": "DtT35D1Vy",
        "status": "complete",
        "createdAt": "2024-04-09T19:00:24.895Z",
        "modifiedAt": "2024-04-09T19:00:24.895Z"
    }
}
 */
