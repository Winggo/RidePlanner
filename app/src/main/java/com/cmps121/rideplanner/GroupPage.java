package com.cmps121.rideplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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
    // ACTION BAR
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        // ACTION BAR
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(bottomListener);

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
                            Intent intent2 = new Intent(getBaseContext(), CreatingGroups.class);
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

    public void onViewMembers(View view) {
        Intent intent = new Intent(this, ViewMembers.class);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupCode", groupCode);
        startActivity(intent);
    }
}
