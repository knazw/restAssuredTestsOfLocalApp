package org.cypress.example.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserCreated {
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserCreated{" +
                "id='" + id + '\'' +
                ", uuid='" + uuid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", createdAt='" + createdAt + '\'' +
                ", modifiedAt='" + modifiedAt + '\'' +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
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

    public String uuid;
    public String firstName;
    public String lastName;
    public String username;
    public String password;
    public int balance;
    public String createdAt;
    public String modifiedAt;


        /*"id": "_9n5n0SPC",
                "uuid": "f4684290-cba6-4654-8efc-f9a7df09ea7b",
                "firstName": "Michal",
                "lastName": "Nazwisko",
                "username": "notexistingUser",
                "password": "$2a$10$/GQh2BZG9XeuYVTZ.HIDdOrwi7KIxGe2K330Q1IXwl5avK41U94Ce",
                "balance": 0,
                "createdAt": "2024-04-06T18:41:44.785Z",
                "modifiedAt": "2024-04-06T18:41:44.785Z"

         */
}

