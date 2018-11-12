package com.cmps121.rideplanner;

import java.util.HashMap;

public class User {
    String userID;
    String userName;
    String userPhoneNumber;
    String userAddress;
    Boolean profileCreated;
    HashMap<String, Boolean> groups;

    public User(String userID, String userName, Boolean profileCreated, HashMap<String, Boolean> groups) {
        this.profileCreated = profileCreated;
        this.userID = userID;
        this.userName = userName;
        this.groups = groups;
    }

    public User(String userID, String userName, String userPhoneNumber, String userAddress, Boolean profileCreated, HashMap<String, Boolean> groups) {
        this.userID = userID;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhoneNumber = userPhoneNumber;
        this.profileCreated = profileCreated;
        this.groups = groups;
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
