package com.cmps121.rideplanner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class JoiningGroups extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference dbUsers;
    DatabaseReference dbGroups;

    FirebaseUser user;
    String userID;

    EditText codeInput;

    GenericTypeIndicator<Map<String, Boolean>> genericTypeIndicator;

    Boolean foundGroup;
    Button joinButton;
    String groupCode;
    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining_groups);
        db = FirebaseDatabase.getInstance();
        dbGroups = db.getReference("groups");
        genericTypeIndicator = new GenericTypeIndicator<Map<String, Boolean>>() {};

        foundGroup = false;
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        codeInput = (EditText) findViewById(R.id.codeInput);

        joinButton = (Button) findViewById(R.id.joinBtn);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onJoinButton(view);
            }
        });
    }

    public void onJoinButton(View view) {
        // get user input code
        groupCode = codeInput.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference("groups")
                .orderByChild("groupCode")
                .equalTo(groupCode);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Boolean> members = new HashMap<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    foundGroup = true;
                    groupName = ds.child("groupName").getValue().toString();

                    if (ds.child("members").getValue(genericTypeIndicator) != null) {
                        members = ds.child("members").getValue(genericTypeIndicator);
                    }
                    // add userID to the members and update the value
                    members.put(userID, true);
                    ds.getRef().child("members").setValue(members);
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
                            Map<String, Boolean> groups = new HashMap<>();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.child("groups").getValue(genericTypeIndicator) != null) {
                                    groups = ds.child("groups").getValue(genericTypeIndicator);
                                }
                                groups.put(groupName, true);
                                ds.getRef().child("groups").setValue(groups);
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
