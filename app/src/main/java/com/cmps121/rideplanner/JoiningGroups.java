package com.cmps121.rideplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class JoiningGroups extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference dbUsers;
    DatabaseReference dbGroups;

    FirebaseUser user;

    EditText codeInput;

    Boolean foundGroup;
    Button joinButton;
    String groupCode;
    String groupName;

    String userID;
    String userName;
    String userAddress;
    String userPhoneNumber;
//     ACTION BAR
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining_groups);

//         ACTION BAR
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(bottomListener);

        db = FirebaseDatabase.getInstance();
        dbGroups = db.getReference("groups");
        dbUsers = db.getReference("users");

        foundGroup = false;
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

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

                    }       catch(NullPointerException e){
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


        codeInput = (EditText) findViewById(R.id.codeInput);

        joinButton = (Button) findViewById(R.id.joinBtn);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onJoinButton(view);
            }
        });
    }

//     ACTION BAR
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
//                        case R.id.logout:
//                            signOut(getWindow().getDecorView().getRootView());
//                            return true;
                    }
                    return false;
                }
            };

    public void onJoinButton(View view) {
        // get user input code
        groupCode = codeInput.getText().toString();

        // find the group
        Query query = FirebaseDatabase.getInstance().getReference("groups")
                .orderByChild("groupCode")
                .equalTo(groupCode);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> members = new HashMap<>();
                // build the group User object
                User user = new User(userID, userName, userPhoneNumber, userAddress, false);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    foundGroup = true;
                    groupName = ds.child("groupName").getValue().toString();

                    // add userID to the members and update the value
                    members.put(userID, user);
                    ds.getRef().child("members").updateChildren(members);
                }

                if(!foundGroup) {
                    Toast.makeText(getApplicationContext(), "Invalid group ID. Please make sure you have a correct group ID", Toast.LENGTH_LONG).show();
                }
                else {
                    // if the group existed, then add the group to the user info also
                    Query userQuery = FirebaseDatabase.getInstance().getReference("users")
                            .orderByChild("userID")
                            .equalTo(userID);

                    ValueEventListener userValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> groups = new HashMap<>();
                            Map<String, Object> events = new HashMap<>();
                            // add the group to the user field with a temporary event field
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                events.put("init", false);
                                groups.put(groupName, true);
                                ds.child("groups").getRef().updateChildren(groups);
                                ds.getRef().child("groups").child(groupName).child("events").updateChildren(events);

                                Intent intent = new Intent(getApplicationContext(), GroupPage.class);
                                intent.putExtra("groupClicked", groupName);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };

                    userQuery.addListenerForSingleValueEvent(userValueEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addListenerForSingleValueEvent(valueEventListener);

    }

}
