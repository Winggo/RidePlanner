package com.cmps121.rideplanner;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
    String eventLocation;

    // ACTION BAR
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invites);

        // ACTION BAR
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(bottomListener);

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
                    String groupCode = ds.getKey();
                    String eventName;
                    for (DataSnapshot data : ds.child("events").getChildren()) {
                        eventName = data.getKey();
                        EventInviteListItem event = new EventInviteListItem(groupName, groupCode, eventName);
                        events.add(event);
                    }
                }
                EventInviteAdapter adapter = new EventInviteAdapter(ViewInvites.this, events);

                listView = findViewById(R.id.invitesList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // ACTION BAR
    private BottomNavigationView.OnNavigationItemSelectedListener bottomListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()){
                        case R.id.mainActivity:
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                            return true;
                        case R.id.joinCreateGroups:
                            Intent intent2 = new Intent(getBaseContext(), JoinCreateGroup.class);
                            startActivity(intent2);
                            return true;
                        case R.id.existingGroups:
                            Intent intent3 = new Intent(getBaseContext(), ViewGroups.class);
                            startActivity(intent3);
                            return true;
                        case R.id.viewInvites:
                            Intent intent4 = new Intent(getBaseContext(), ViewInvites.class);
                            startActivity(intent4);
                            return true;
                        case R.id.editProfile:
                            Intent intent5 = new Intent(getBaseContext(), EditProfile.class);
                            startActivity(intent5);
                            return true;
                    }
                    return false;
                }
            };
}
