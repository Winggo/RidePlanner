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


public class CarGen  extends AsyncTask<String, Void, Bitmap> {
//    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("groups/");

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener
    ArrayList<User> profiles = new ArrayList<>();



    protected Bitmap doInBackground(String... strings) {
//        super.doInBackground();
        mAuth.addAuthStateListener(mAuthListener);
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

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            User uInfo = new User();
            uInfo.setUserID(ds.child("Admin").getValue(User.class).getName()); //set the name
            uInfo.setUserAddress(ds.child("Admin").getValue(User.class).getEmail()); //set the email
            uInfo.setUserPhoneNumber(ds.child("Admin").getValue(User.class).getUserPhoneNumber()); //set the phone_num

        }




    @Override
    protected void onPostExecute(Bitmap bitmap) {
//        super.onPostExecute(bitmap);
//        byte[] image = MyDB.getBytes(bitmap);
//        getImage.db.addEntry("Uh oh", image);
        saveImage(mContext, bitmap, makepath+".jpg");
        getImage.pathholder = makepath+".jpg";
////        MainActivity.imageView.setImageBitmap(bitmap);
    }


    }
