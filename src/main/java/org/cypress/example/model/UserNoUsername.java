package org.cypress.example.model;

public class UserNoUsername {
//    public String id;
//    public String uuid;
    public String firstName;
    public String lastName;
    public String password;
    public String confirmPassword;
//    public String email;
//    public String phoneNumber;
//    public String avatar;
//    public String defaultPrivacyLevel;
//    public int balance;
//    public String createdAt;
//    public String modifiedAt;

    public UserNoUsername() {

    }
    //public UserNoUsername(String id, String uuid, String firstName, String lastName, String password, String email, String phoneNumber, String avatar, String defaultPrivacyLevel, int balance, String createdAt, String modifiedAt) {
    public UserNoUsername(String firstName, String lastName, String password, String confirmPassword) {
//        this.id = id;
//        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
//        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        /*this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.defaultPrivacyLevel = defaultPrivacyLevel;
        this.balance = balance;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;

         */
    }
/*
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
    */

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

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
/*
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDefaultPrivacyLevel() {
        return defaultPrivacyLevel;
    }

    public void setDefaultPrivacyLevel(String defaultPrivacyLevel) {
        this.defaultPrivacyLevel = defaultPrivacyLevel;
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

*/
    /*
                "id": "t45AiwidW",
            "uuid": "6383f84e-b511-44c5-a835-3ece1d781fa8",
            "firstName": "Edgar",
            "lastName": "Johns",
            "username": "Katharina_Bernier",
            "password": "$2a$10$5PXHGtcsckWtAprT5/JmluhR13f16BL8SIGhvAKNP.Dhxkt69FfzW",
            "email": "Norene39@yahoo.com",
            "phoneNumber": "625-316-9882",
            "avatar": "https://cypress-realworld-app-svgs.s3.amazonaws.com/t45AiwidW.svg",
            "defaultPrivacyLevel": "public",
            "balance": 168137,
            "createdAt": "2019-08-27T23:47:05.637Z",
            "modifiedAt": "2020-05-21T11:02:22.857Z"
     */
}
