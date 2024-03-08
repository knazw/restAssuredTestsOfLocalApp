package org.cypress.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateNotification {
    public String id;



    public boolean isRead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty(value="isRead")
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

}
