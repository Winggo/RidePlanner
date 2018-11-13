package com.cmps121.rideplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GroupPage extends AppCompatActivity {

    String groupName;
    String groupCode;
    TextView groupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        // get reference of title textview
        groupTitle = (findViewById(R.id.groupName));

        // Grab group name from intent extras
        groupName = getIntent().getStringExtra("groupClicked");

        Query groupCodeQuery = FirebaseDatabase.getInstance().getReference("groups")
                .orderByChild("groupName")
                .equalTo(groupName);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    groupCode = ds.child("groupCode").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        groupCodeQuery.addListenerForSingleValueEvent(valueEventListener);
        // Display the group name
        groupTitle.setText(groupName);
    }

    public void onCreateEvent(View view) {
        Intent intent = new Intent(this, CreateEventPopUp.class);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupCode", groupCode);
        startActivity(intent);
    }

    public void onViewEvents(View view) {
        Intent intent = new Intent(this, ViewEvents.class);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupCode", groupCode);
        startActivity(intent);
    }
}
