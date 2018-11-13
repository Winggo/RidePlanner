package com.cmps121.rideplanner;

import java.util.ArrayList;

public class User {
    String userID;
    String userName;
    String userPhoneNumber;
    static String userAddress;
    Boolean profileCreated;
    //stores the closer people from least to greatest
  static  ArrayList<User> closer;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

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
    // uInfo.setID(ds.child("Admin").getValue(User.class).getName()); //set the name
    //            uInfo.setAddress(ds.child("Admin").getValue(User.class).getEmail()); //set the email
    //            uInfo.setPhone_num(

    public String getUserID() {
        return userID;
    }



    public static ArrayList<User> getCloserAddress(){
        return closer;

    }
    public static User getCloserAddressSingle(int c){
        return closer.get(c);

    }

    public static void setCloserAddress(User c){
        closer.add(c);
//        return closer;

    }
    public static void setCloserAddressAll(ArrayList<User> c){
       closer = c;
//        return closer;

    }
//    public void setcloserride(double c){
//        User.setCloserAddress(c);
//
//    }


    public String setUserID(String set){
        return userID =  set;
    }

    public String getUserName() {
        return userName;
    }
    public String setUserName(String set){
        return userName = set;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String setUserPhoneNumber(String set){
        userID = set;
        return userID;
    }
    public static String getUserAddress() {
        return userAddress;
    }

    public static String setUserAddress(String set){
        return userAddress = set;
    }
    public Boolean getProfileCreated() { return profileCreated; }
}
