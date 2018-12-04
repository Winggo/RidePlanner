package com.cmps121.rideplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;

public class ViewEvents extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView eventsList;
    FirebaseDatabase db;
    DatabaseReference dbGroups;
    FirebaseUser user;
    String userID;
    String groupName;
    String groupCode;
    // ACTION BAR
    private ActionBar toolbar;

    GenericTypeIndicator<Map<String, Boolean>> genericTypeIndicator;
    Map<String, Boolean> eventsMap;
    List<String> events;

    TextView groupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        // ACTION BAR
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(bottomListener);

        // initialize the instances of db and current user
        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        // grab group name and group code
        groupName = getIntent().getStringExtra("groupName");
        groupCode = getIntent().getStringExtra("groupCode");

        // initialize events as a new arraylist for displaying later
        events = new ArrayList<>();

        // get the list view and set on item clicks
        eventsList = (ListView) findViewById(R.id.eventList);
        eventsList.setOnItemClickListener(this);

        groupTitle = findViewById(R.id.groupTitle);
        groupTitle.setText(groupName);

        // need GenericTypeIndicator in order to get groups HashMap from DB later
        genericTypeIndicator = new GenericTypeIndicator<Map<String, Boolean>>() {};
        eventsMap = new HashMap<>();

        dbGroups = db.getReference("groups");

        // get all events that the user is in
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("userID")
                .equalTo(userID);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("groups").child(groupName).child("events").getValue(genericTypeIndicator) != null) {
                        eventsMap = ds.child("groups").child(groupName).child("events").getValue(genericTypeIndicator);
                    }

                    // loop through all the keys and add it to groups ArrayList for display

                    for (String key : eventsMap.keySet()) {
                        if(eventsMap.get(key) == true) {
                            events.add(key);
                        }
                    }
                }

                // display this list
                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, events) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView)view.findViewById(android.R.id.text1);
                        text.setTextColor(getResources().getColor(R.color.featuresColor));
                       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            text.setTypeface(getResources().getFont(R.font.heebo_thin));
                        }*/
                        return view;
                    }
                };
                eventsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // perform query and fetch data
        query.addListenerForSingleValueEvent(valueEventListener);
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

    @Override
    public void onItemClick(AdapterView<?> list, View view, int position, long id) {
        // get the name of the group that was clicked
        String eventName = (String) list.getItemAtPosition(position);
        // pass it to GroupPage activity
        Intent intent = new Intent(this, EventPage.class);
        intent.putExtra("eventName", eventName);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupCode", groupCode);

        startActivity(intent);
    }
}
