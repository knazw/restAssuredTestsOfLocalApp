package org.cypress.example.model;

import java.util.List;

public class TransactionGet {
    public String receiverName;
    public String senderName;
    public List<Like> likes;
    public List<String> comments;
    public String id;
    public String uuid;
    public int amount;
    public String description;
    public String receiverId;
    public String senderId;
    public String status;
    public String requestStatus;
    public String createdAt;
    public String modifiedAt;

    public TransactionGet() {

    }
    public TransactionGet(String receiverName, String senderName, List<Like> likes, List<String> comments, String id, String uuid, int amount, String description, String receiverId, String senderId, String status, String requestStatus,String createdAt, String modifiedAt) {
        this.receiverName = receiverName;
        this.senderName = senderName;
        this.likes = likes;
        this.comments = comments;
        this.id = id;
        this.uuid = uuid;
        this.amount = amount;
        this.description = description;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.status = status;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
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

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
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


    /*
    {
            "receiverName": "Jan Kowalski",
            "senderName": "Adam Nowak",
            "likes": [

            ],
            "comments": [

            ],
            "id": "3KBcbjhFC",
            "uuid": "50bcf3c7-2418-40f1-a3bf-d15d1d6a6572",
            "amount": 10000,
            "description": "note 1",
            "receiverId": "-ITlVVG4_",
            "senderId": "bs_xf2pmt",
            "status": "complete",
            "createdAt": "2024-04-09T19:45:05.607Z",
            "modifiedAt": "2024-04-09T19:45:05.607Z"
        }
     */
}
