package com.cmps121.rideplanner;


import android.graphics.Bitmap;

import java.util.ArrayList;

//Reference:http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-address-and-text-using-arrayadapter/#ListView_Row_layout_XML_list_itemxml


//Goal is to have an object view instead of a string or an int only


public class RideGroupManager {
    private String address;
    private String title;
    private String id;
    private ArrayList<User> Riders;

    public RideGroupManager(){

    }

    public RideGroupManager(String address1, String title, String id) {
        this.address = address1;
        this.title = title;
        this.id = id;
    }

    public ArrayList<User> getCar()
    {
        return Riders;
    }
    public void setCar(User user)
    {
        Riders.add(user);
    }
    public void setCarAll(ArrayList<User> userall)
    {
       Riders = userall;
    }
    public String getaddress()
    {
        return address;
    }
    public void setaddress(String address)
    {
        this.address = address;
    }
    public String getid()
    {
        return id;
    }
    public void setid(String id) {

        this.id = id;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + id;
    }
}

