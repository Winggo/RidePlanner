package com.cmps121.rideplanner;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewInvites extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference dbUsers;
    DatabaseReference dbGroups;
    String userID;
    FirebaseUser user;
    ArrayList<EventInviteListItem> events;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invites);

        db = FirebaseDatabase.getInstance();
        dbUsers = db.getReference("users");
        dbGroups = db.getReference("groups");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        events = new ArrayList<>();

        dbUsers.child(userID).child("eventInvites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // for each group found, grab all events and add them to the arraylist
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String groupName = ds.child("groupName").getValue().toString();
                    String eventLocation  = ds.child("eventLocation").getValue().toString();
                    String groupCode = ds.getKey();
                    String eventName;
                    for (DataSnapshot data : ds.child("events").getChildren()) {
                        eventName = data.getKey();
                        EventInviteListItem event = new EventInviteListItem(groupName, groupCode, eventLocation, eventName);
                        events.add(event);
                    }
                }
                EventInviteAdapter adapter = new EventInviteAdapter(getApplicationContext(), events);

                listView = findViewById(R.id.invitesList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
