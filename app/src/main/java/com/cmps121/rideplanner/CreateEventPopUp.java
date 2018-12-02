package com.cmps121.rideplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CreateEventPopUp extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference dbGroups;
    DatabaseReference dbUsers;
    FirebaseUser user;

    EditText eventNameInput;
    EditText eventDescriptionInput;
    EditText eventLocationInput;

    String eventName;
    String eventDescription;
    String eventLocation;
    String userID;
    String groupName;
    String groupCode;

    String userName;
    String userAddress;
    String userPhoneNumber;

    GenericTypeIndicator<Map<String, Boolean>> genericTypeIndicator;
    Map<String, Boolean> events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_pop_up);

        genericTypeIndicator = new GenericTypeIndicator<Map<String, Boolean>>() {};
        events = new HashMap<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        db = FirebaseDatabase.getInstance();
        dbGroups = db.getReference("groups");
        dbUsers = db.getReference("users");

        eventNameInput = findViewById(R.id.eventNameInput);
        eventDescriptionInput = findViewById(R.id.descriptionInput);
        eventLocationInput = findViewById(R.id.editLoc);

        groupName = getIntent().getStringExtra("groupName");
        groupCode = getIntent().getStringExtra("groupCode");

        Query query = dbUsers
                .orderByChild("userID")
                .equalTo(userID);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userName = ds.child("userName").getValue().toString();
                    try {
                        userAddress = ds.child("address").getValue().toString();
                        userPhoneNumber = ds.child("phoneNumber").getValue().toString();

                    } catch (NullPointerException e) {
                        Toast.makeText(getApplicationContext(), "Please set up your user profile!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    public void onCreateEventBtn(View view) {
        Map<String, User> attendees = new HashMap<>();
        User user = new User(true, false, userID, userName, userPhoneNumber, userAddress);
        attendees.put(userID, user);

        eventName = eventNameInput.getText().toString();
        eventDescription = eventDescriptionInput.getText().toString();
        eventLocation = eventLocationInput.getText().toString();
        Event event = new Event(eventName, eventDescription, eventLocation, attendees);

        dbGroups.child(groupCode).child("events").child(eventName).setValue(event);

        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("userID")
                .equalTo(userID);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("groups").child(groupName).child("events").getValue(genericTypeIndicator) != null) {
                        events = ds.child("groups").child(groupName).child("events").getValue(genericTypeIndicator);
                    }

                    // remove initial init event
                    if (events.containsKey("init")) {
                        events.remove("init");
                    }
                    events.put(eventName, true);
                    ds.getRef().child("groups").child(groupName).child("events").setValue(events);
                }
                Intent intent = new Intent(getApplicationContext(), ViewEvents.class);
                intent.putExtra("groupName", groupName);
                intent.putExtra("groupCode", groupCode);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addListenerForSingleValueEvent(valueEventListener);
    }
}
