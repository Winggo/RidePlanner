package com.cmps121.rideplanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

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
import java.util.Arrays;


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
            uInfo.setUserAddress(""+3;  //set the email
            uInfo.setUserName("TinTin");
            uInfo.setUserPhoneNumber("911"); //set the phone_num

//            User uInfo1 = new User();
        uInfo1.setUserAddress(""+6); //set the email
        uInfo1.setUserName("TinTin");
        uInfo1.setUserPhoneNumber("911"); //set the phone_num

//            User uInfo2 = new User();
        uInfo2.setUserAddress((""+2)); //set the email
        uInfo2.setUserName("TinTin");
        uInfo2.setUserPhoneNumber("911"); //set the phone_num

        uInfo3.setUserAddress((""+10)); //set the email
        uInfo3.setUserName("TinTin");
        uInfo3.setUserPhoneNumber("911"); //set the phone_num


        uInfo4.setUserAddress("107 Nobel Drive, Santa Cruz"); //set the email
        uInfo4.setUserAddress(""+1); //set the email
        uInfo4.setUserName("TinTin");
        uInfo4.setUserPhoneNumber("911"); //set the phone_num
        return "";
        }


        public ArrayList<User> sortGetLowest() {
            profiles.add(uInfo1);
            profiles.add(uInfo2);
            profiles.add(uInfo3);
            profiles.add(uInfo4);
            profiles.add(uInfo);
            double holder;
            User publica;

            //get the amount of distance and holding the next cloest person
            for (int i = 0; i < profiles.size(); i++) {
                for (int j = 0; j < profiles.size(); j++) {
//                    holder = Integer.parseDouble(profiles.get(j).getUserAddress());
                    publica = profiles.get(j);
//                    profiles.get(i).setCloserAddress(holder);
                    profiles.get(i).setCloserAddress(publica);
                }
            }
            Integer [] bar;
            User [] bar1;
            for (int i = 0; i < profiles.size(); i++) {
//                profiles.get(i).setCloserAddress(i);
//                bar = profiles.get(i).getCloserAddress().toArray(new Integer[profiles.size()]);
                bar1 = profiles.get(i).getCloserAddress().toArray(new User[profiles.size()]);
                sort(bar1, 0, bar1.length-1);
            }

            ArrayList<User> arrayList = new ArrayList<User>(Arrays.asList(bar1));
            return arrayList;

        }
        int partition(User[] arr, int low, int high)
            {
                int pivot = Integer.parseInt(arr[high].getUserAddress());
                int i = (low-1); // index of smaller element
                for (int j=low; j<high; j++)
                {
                    // If current element is smaller than or
                    // equal to pivot
                    if (Integer.parseInt(arr[j].getUserAddress()) <= pivot)
                    {
                        i++;

                        // swap arr[i] and arr[j]
                        int temp = Integer.parseInt(arr[i].getUserAddress());
                        int tempi =  Integer.parseInt(arr[i].getUserAddress());
                        arr[i].setUserAddress(Integer.parseInt(arr[high].getUserAddress(j)));
                        Integer.parseInt(arr[high].getUserAddress(j))= temp;
                    }
                }

                // swap arr[i+1] and arr[high] (or pivot)
                int temp = arr[i+1];
                arr[i+1] = arr[high];
                arr[high] = temp;

                return i+1;
            }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    void sort(User arr[], int low, int high)
            {
                if (low < high)
                {
            /* pi is partitioning index, arr[pi] is
              now at right place */
                    int pi = partition(arr, low, high);

                    // Recursively sort elements before
                    // partition and after partition
                    sort(arr, low, pi-1);
                    sort(arr, pi+1, high);
                }
            }

            //then start throwing people into the cars
            // look for the smallest arraylist that has lowest distance betwen the users
            // and then work from there for rides

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
        public void letsGetCars(ArrayList<Double>){


        }

    @Override
    protected void onPostExecute(String help) {
////        super.onPostExecute(bitmap);
////        byte[] image = MyDB.getBytes(bitmap);
////        getImage.db.addEntry("Uh oh", image);
//        saveImage(mContext, bitmap, makepath+".jpg");
//        getImage.pathholder = makepath+".jpg";
//////        MainActivity.imageView.setImageBitmap(bitmap);


            }


    }
