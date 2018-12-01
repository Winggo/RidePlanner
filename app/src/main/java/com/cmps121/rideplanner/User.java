package com.cmps121.rideplanner;

import java.util.HashMap;

public class User {
    static String userID;
    static String userName;
    static String phoneNumber;
    static String address;
    static Boolean moderator;
    static Boolean eventModerator;
    static Boolean driver;
    static Boolean profileCreated;
    static HashMap<String, Boolean> groups;

    public User() {
    }

    public User(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public User(String userID, String userName, Boolean profileCreated, HashMap<String, Boolean> groups) {
        this.profileCreated = profileCreated;
        this.userID = userID;
        this.userName = userName;
        this.groups = groups;
    }

    public User(String userID, String userName, String phoneNumber, String address, Boolean profileCreated, HashMap<String, Boolean> groups) {
        this.userID = userID;
        this.userName = userName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profileCreated = profileCreated;
        this.groups = groups;
    }

    // user constructor to be used in groups
    public User(String userID, String userName, String phoneNumber, String address, Boolean moderator) {
        this.userID = userID;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.moderator = moderator;
    }

    // user constructor to be used in events
    public User(Boolean eventModerator, Boolean driver, String userID, String userName, String phoneNumber, String address) {
        this.userID = userID;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.eventModerator = eventModerator;
        this.driver = driver;
    }



    public static String getUserID() { return userID; }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getProfileCreated() { return profileCreated; }


    public String setUserID(String set){
        return userID =  set;
    }


    public String setUserName(String set){
        return userName = set;
    }


    public String setUserPhoneNumber(String set){
        userID = set;
        return userID;
    }


    public String setUserAddress(String set){
        return address = set;
    }

}
