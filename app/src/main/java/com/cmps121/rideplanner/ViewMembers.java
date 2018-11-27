package com.cmps121.rideplanner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;

public class ViewMembers extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference dbGroups;
    DatabaseReference dbGroup;
    DatabaseReference dbMembers;

    String groupName;
    String groupCode;

    TextView viewMembersTitle;
    ListView memberList;
    String userID;

    GenericTypeIndicator<Map<String, Boolean>> genericTypeIndicator;
    Map<String, Boolean> membersMap;
    List<String> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);

        db = FirebaseDatabase.getInstance();

        groupName = getIntent().getStringExtra("groupName");
        groupCode = getIntent().getStringExtra("groupCode");

        viewMembersTitle = findViewById(R.id.viewMembersTitle);
        viewMembersTitle.setText("Members of " +groupName);

        genericTypeIndicator = new GenericTypeIndicator<Map<String, Boolean>>() {};
        members = new ArrayList<>();
        membersMap = new HashMap<>();

        memberList = findViewById(R.id.memberList);

        dbGroups = db.getReference("groups");
        dbGroup = dbGroups.child(groupCode);
        dbMembers = dbGroup.child("members");

        Query query = dbGroups
                .orderByChild("groupCode")
                .equalTo(groupCode);

        dbMembers.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    members.add(user.getUserName());
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, members);
                memberList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
