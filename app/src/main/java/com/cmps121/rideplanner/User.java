package com.cmps121.rideplanner;

public class User {
    String userID;
    String userName;
    String userPhoneNumber;
    String userAddress;
    Boolean profileCreated;

    public User(String userID, String userName, Boolean profileCreated) {
        this.profileCreated = profileCreated;
        this.userID = userID;
        this.userName = userName;
    }

    public User(String userID, String userName, String userPhoneNumber, String userAddress, Boolean profileCreated) {
        this.userID = userID;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhoneNumber = userPhoneNumber;
        this.profileCreated = profileCreated;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public Boolean getProfileCreated() { return profileCreated; }
}
