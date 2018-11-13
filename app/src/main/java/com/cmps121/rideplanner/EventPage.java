package com.cmps121.rideplanner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EventPage extends AppCompatActivity {

    String eventName;
    String groupName;
    String groupCode;
    String eventDescription;

    TextView eventPageGroupTitle;
    TextView eventPageEventTitle;
    TextView eventPageDescription;

    FirebaseDatabase db;
    DatabaseReference groupsRef;
    DatabaseReference dbGroup;
    DatabaseReference eventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        db = FirebaseDatabase.getInstance();


        groupName = getIntent().getStringExtra("groupName");
        eventName = getIntent().getStringExtra("eventName");
        groupCode = getIntent().getStringExtra("groupCode");

        Log.d("killme", eventName);
        // set database reference to the current group
        groupsRef = db.getReference("groups");
        dbGroup = groupsRef.child(groupCode);
        eventsRef = dbGroup.child("events");
        // query to get the description of the event

        Query eventDescQuery = eventsRef
                .orderByChild("eventName")
                .equalTo(eventName);

        // grab the event description and set the text appropriately

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("killme", "in ondatachange");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("killme", "in for loop");
                    eventDescription = ds.child("eventDescription").getValue().toString();
                }
                eventPageDescription = findViewById(R.id.eventPageDescription);
                eventPageDescription.setText(eventDescription);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // perform query
        eventDescQuery.addListenerForSingleValueEvent(valueEventListener);

        // load all the event info and display it
        eventPageGroupTitle = findViewById(R.id.eventPageGroupTitle);
        eventPageEventTitle = findViewById(R.id.eventPageEventTitle);

        eventPageGroupTitle.setText(groupName);
        eventPageEventTitle.setText(eventName);


    }
}
