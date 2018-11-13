package com.cmps121.rideplanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class CarGen  extends AsyncTask<String, Void, String> {
//    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("groups/");

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener
    ArrayList<User> profiles = new ArrayList<>();
    User uInfo1,uInfo2,uInfo,uInfo3, uInfo4 = new User();



    protected String doInBackground(String... strings) {
//        super.doInBackground();
//        mAuth.addAuthStateListener(mAuthListener);
//        onDataChange(datasnapshot);
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//            User uInfo = new User();
            uInfo.setUserID("awefwaefewf"); //set the name
            uInfo.setUserAddress("702 College Nine Road, Santa Cruz"); //set the email
            uInfo.setUserName("TinTin");
            uInfo.setUserPhoneNumber("911"); //set the phone_num

//            User uInfo1 = new User();
        uInfo1.setUserAddress("123 Pacific Avenue, Santa Cruz"); //set the email
        uInfo1.setUserName("TinTin");
        uInfo1.setUserPhoneNumber("911"); //set the phone_num

//            User uInfo2 = new User();
        uInfo2.setUserAddress("123 Mission Street, Santa Cruz"); //set the email
        uInfo2.setUserName("TinTin");
        uInfo2.setUserPhoneNumber("911"); //set the phone_num

        uInfo3.setUserAddress("106 Peach Terrace, Santa Cruz"); //set the email
        uInfo3.setUserName("TinTin");
        uInfo3.setUserPhoneNumber("911"); //set the phone_num


        uInfo4.setUserAddress("107 Nobel Drive, Santa Cruz"); //set the email
        uInfo4.setUserName("TinTin");
        uInfo4.setUserPhoneNumber("911"); //set the phone_num
        return "";
        }
    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

//     public String getUserID() {
//        return userID;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public String getUserPhoneNumber() {
//        return userPhoneNumber;
//    }
//
//    public String getUserAddress() {
//        return userAddress;
//    }
//
//    public Boolean getProfileCreated() { return profileCreated; }

//    public void onDataChange(DataSnapshot dataSnapshot) {
//        // This method is called once with the initial value and again
//        // whenever data at this location is updated.
//        showData(dataSnapshot);
//    }
//
//    private void showData(DataSnapshot dataSnapshot) {
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
////            User uInfo = new User();
//            uInfo.setUserID(ds.child("Admin").getValue(User.class).getUserID()); //set the name
//            uInfo.setUserAddress(ds.child("Admin").getValue(User.class).getUserAddress()); //set the email
//            uInfo.setUserPhoneNumber(ds.child("Admin").getValue(User.class).getUserPhoneNumber()); //set the phone_num
//
////            User uInfo1 = new User();
//            uInfo1.setUserID(ds.child("Thompson").getValue(User.class).getUserID()); //set the name
//            uInfo1.setUserAddress(ds.child("Thompson").getValue(User.class).getUserAddress()); //set the email
//            uInfo1.setUserPhoneNumber(ds.child("Thompson").getValue(User.class).getUserPhoneNumber()); //set the phone_num
//
////            User uInfo2 = new User();
//            uInfo2.setUserID(ds.child("TinTin").getValue(User.class).getUserID()); //set the name
//            uInfo2.setUserAddress(ds.child("TinTin").getValue(User.class).getUserAddress()); //set the email
//            uInfo2.setUserPhoneNumber(ds.child("TinTin").getValue(User.class).getUserPhoneNumber()); //set the phone_num
//
//        }
//
//    }



    @Override
    protected void onPostExecute(String help) {
////        super.onPostExecute(bitmap);
////        byte[] image = MyDB.getBytes(bitmap);
////        getImage.db.addEntry("Uh oh", image);
//        saveImage(mContext, bitmap, makepath+".jpg");
//        getImage.pathholder = makepath+".jpg";
//////        MainActivity.imageView.setImageBitmap(bitmap);

            profiles.add(uInfo1);
            profiles.add(uInfo2);
            profiles.add(uInfo3);
            profiles.add(uInfo4);
            profiles.add(uInfo);
            }


    }
