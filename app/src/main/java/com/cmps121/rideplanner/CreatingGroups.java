package com.cmps121.rideplanner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreatingGroups extends AppCompatActivity {

    FirebaseDatabase db;
    FirebaseUser user;
    DatabaseReference dbGroups;

    EditText groupInput;
    Button createButton;
    TextView shareTitle;
    TextView groupCode;
    TextView shareText;

    String groupName;
    String userID;

    GenericTypeIndicator<Map<String, Boolean>> genericTypeIndicator;
    Map<String, Boolean> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_groups);
        genericTypeIndicator = new GenericTypeIndicator<Map<String, Boolean>>() {};

        db = FirebaseDatabase.getInstance();
        dbGroups = db.getReference("groups");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        groups = new HashMap<>();

        groupInput = (EditText) findViewById(R.id.groupField);
        createButton = (Button) findViewById(R.id.createBtn);
        shareTitle = (TextView) findViewById(R.id.shareTitle);
        shareText = (TextView) findViewById(R.id.shareText);
        groupCode = (TextView) findViewById(R.id.groupCode);

        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCreateBtn(view);
            }
        });
    }

    public void onCreateBtn(View view) {
        groupName = groupInput.getText().toString();
        String groupID = dbGroups.push().getKey();
        groupCode.setText(groupID);
        Map<String, Boolean> members = new HashMap<>();
        members.put(userID, true);

        Group group = new Group(groupName, groupID);
        dbGroups.child(groupID).setValue(group);
        dbGroups.child(groupID).child("members").setValue(members);

      //  groups = new HashMap<String, Boolean>();

        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("userID")
                .equalTo(userID);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

        query.addListenerForSingleValueEvent(valueEventListener);

        // set all the views as visible and display the group code
        shareTitle.setVisibility(View.VISIBLE);
        groupCode.setVisibility(View.VISIBLE);
        shareText.setVisibility(View.VISIBLE);

    }
}
