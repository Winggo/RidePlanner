package com.cmps121.rideplanner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class viewAllCars extends AppCompatActivity {
    ArrayList<User> profiles = new ArrayList<>();
    static User uInfo1,uInfo2,uInfo,uInfo3, uInfo4 = new User();

    private Context context;
    private ListView itemsList;
    //    private ArrayList<String> items;
    private ArrayAdapter<User> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_cars);
    }

    public void listDrivers(View view){
        uInfo.setUserID("awefwaefewf"); //set the name
        uInfo.setUserAddress(""+3);  //set the email
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


        profiles.add(uInfo1);
        profiles.add(uInfo2);
        profiles.add(uInfo3);
        profiles.add(uInfo4);
        profiles.add(uInfo);


        itemsList = findViewById(R.id.list);

//        EnterYourInfo.items = FileManager.readFile(this);
        adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, profiles);
        itemsList.setAdapter(adapter);


//        listView = (ListView) findViewById(R.id.list);
//        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
//                R.layout.list_item, rowItems);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);

    }




}
