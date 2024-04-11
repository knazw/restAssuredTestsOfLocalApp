package org.cypress.example.model;

public class NotificationGet {
    public String userFullName;
    public String id;
    public String uuid;
    public String userId;
    public String transactionId;
    public String likeId;
    public String status;
    public boolean isRead;
    public String createdAt;
    public String modifiedAt;

    public NotificationGet(){

    }

    public NotificationGet(String userFullName, String id, String uuid, String userId, String transactionId, String likeId, String status, boolean isRead, String createdAt, String modifiedAt) {
        this.userFullName = userFullName;
        this.id = id;
        this.uuid = uuid;
        this.userId = userId;
        this.transactionId = transactionId;
        this.likeId = likeId;
        this.status = status;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
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

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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
