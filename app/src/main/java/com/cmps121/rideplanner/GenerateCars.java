package com.cmps121.rideplanner;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GenerateCars extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbEvent;
    Downloader down = new Downloader();

    TextView groupTitleView;
    TextView eventTitleView;

    ListView driverView;

    String groupName;
    String eventName;
    String groupCode;
    DriverListAdapter adapter;

    /* an arraylist of users so that we can get userID and other info if needed.
    * check my EventInviteListItem and EventInviteAdapter for example of
    * just displaying the name of the group
    * without displaying the groupCode*/
    ArrayList<User> driverUsers;
    ArrayList<String> passengers;

    final ArrayList<CarItems> carLOL = new ArrayList<>();
    HashMap<String, Object> carUsers;
    ArrayList<User> attendees;
    ArrayList<String> attendeeNames;
    ArrayList<Integer> selectedMembersIndexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_cars);

        driverView = findViewById(R.id.driverView);

        groupName = getIntent().getStringExtra("groupName");
        eventName = getIntent().getStringExtra("eventName");
        groupCode = getIntent().getStringExtra("groupCode");

        groupTitleView = findViewById(R.id.generateCarsGroupTitle);
        eventTitleView = findViewById(R.id.generateCarsEventTitle);

        groupTitleView.setText(groupName);
        eventTitleView.setText(eventName);

        selectedMembersIndexList = new ArrayList<>();

     //   car = new ArrayList<>();
        driverUsers = new ArrayList<>();
        attendees = new ArrayList<>();
        attendeeNames = new ArrayList<>();

        ArrayList<String> carPushKeys = new ArrayList<>();
        // get the reference to the event we are generating cars in
        dbEvent = db.getReference("groups").child(groupCode).child("events").child(eventName);

        // query all attendees

        dbEvent.child("attendees").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    // if they're a driver, add it to driver list. otherwise, add it to normal attendees
                    if (ds.child("driver").getValue(Boolean.class)) {
                        driverUsers.add(ds.getValue(User.class));
                        HashMap<String, Object> tempDriver = new HashMap<>();
                        ds.child("inCar").getRef().setValue(ds.getValue(User.class).getUserID());
                        tempDriver.put(ds.getValue(User.class).getUserID(), ds.getValue(User.class));

                        String pushedKey = dbEvent.child("cars").push().getKey();
                        tempDriver.put("driverID", ds.getValue(User.class).getUserID());
                        dbEvent.child("cars").child(pushedKey).updateChildren(tempDriver);

                        carLOL.add(new CarItems(ds.getValue(User.class).getUserName(), "Empty", "Empty", "Empty", "Empty"));

                    }
                    else {
                        attendees.add(ds.getValue(User.class));
                    }
                }

                for (int i = 0; i < attendees.size(); i++) {
                    attendeeNames.add(attendees.get(i).getUserName());
                }

                adapter = new DriverListAdapter(GenerateCars.this, carLOL, groupCode, groupName, eventName, driverUsers, dbEvent);
                driverView.setAdapter(adapter);

                /* everything that you do to display this list of drivers or attendees must be done in this
                 * onDataChange method. Firebase does its queries ASYNC and if you try to do it
                 * outside of this onDataChange, you won't retrieve the data appropriately before displaying
                 * if you REALLY wanna do it synchronously, you have to find a way to wait for the query to finish.
                 *
                 *
                 * If you want to display attendees in a new activity popup, on the button click to add
                 * people to a driver's car, you can do a query to grab all attendees and then pass the list
                 * and route to that popup activity. you create a new intent then intent.putExtra(*whatever you're putting)
                 * then startActivity(intent).
                 *
                 * work on displaying the drivers first, then setting the onclick listener for each driver element to pull up the list of attendees */

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
