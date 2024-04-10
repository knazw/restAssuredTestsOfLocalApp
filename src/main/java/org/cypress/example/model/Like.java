package org.cypress.example.model;

public class Like {
    public String id;
    public String uuid;
    public String userId;
    public String transactionId;
    public String createdAt;
    public String modifiedAt;


    public Like() {
    }

    public Like(String id, String uuid, String userId, String transactionId, String createdAt, String modifiedAt) {
        this.id = id;
        this.uuid = uuid;
        this.userId = userId;
        this.transactionId = transactionId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
                    "id": "51CrC-Gohz",
                    "uuid": "97b4f0fb-f6e9-427b-91bd-3bb6049ddf5b",
                    "userId": "cv2FOuWYZ",
                    "transactionId": "FcFcueItss",
                    "createdAt": "2024-04-10T14:28:25.008Z",
                    "modifiedAt": "2024-04-10T14:28:25.008Z"
                }
 */