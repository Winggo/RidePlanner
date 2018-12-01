package com.cmps121.rideplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
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

public class EventPage extends AppCompatActivity {

    String eventName;
    String groupName;
    String groupCode;
    String eventDescription;
    String userID;
    String userName;
    String invitedUserID;

    TextView eventPageGroupTitle;
    TextView eventPageEventTitle;
    TextView eventPageDescription;
    TextView goingMembers;

    Integer numAttendees = 0;
    Switch goingSwitch;
    Switch driverSwitch;
    LinearLayout driverLayout;

    FirebaseDatabase db;
    FirebaseUser user;

    DatabaseReference groupsRef;
    DatabaseReference dbGroup;
    DatabaseReference eventsRef;
    DatabaseReference dbGroups;
    DatabaseReference dbMembers;
    DatabaseReference dbUsers;
    DatabaseReference groupRef;

    GenericTypeIndicator<Map<String, User>> genericTypeIndicator;

    List<String> members = new ArrayList<>();
    List<String> memberIDs = new ArrayList<>();
    Map<String, User> membersMap = new HashMap<>();
    CharSequence[] memberSequence;



    private ArrayList<Integer> selectedMembersIndexList;


    protected void goToCars(View view){
        Intent intent = new Intent(this, viewAllCars.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        db = FirebaseDatabase.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        genericTypeIndicator = new GenericTypeIndicator<Map<String, User>>() {};


        groupName = getIntent().getStringExtra("groupName");
        eventName = getIntent().getStringExtra("eventName");
        groupCode = getIntent().getStringExtra("groupCode");
        goingMembers = findViewById(R.id.goingMembers);
        goingSwitch = findViewById(R.id.goingSwitch);
        driverLayout = findViewById(R.id.canDriveLayout);
        driverSwitch = findViewById(R.id.canDriveSwitch);

        // set database reference to the current group
        groupsRef = db.getReference("groups");
        dbGroup = groupsRef.child(groupCode);
        eventsRef = dbGroup.child("events");
        dbUsers = db.getReference("users");


        // querying the specific event

        Query eventDescQuery = eventsRef
                .orderByChild("eventName")
                .equalTo(eventName);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // grabbing the event description
                    eventDescription = ds.child("eventDescription").getValue().toString();

                    // counting the number of attendees
                    numAttendees = (int) ds.child("attendees").getChildrenCount();
                    // if our current user is attending the event, set switch to true
                    if (ds.child("attendees").child(userID).exists()) {
                        goingSwitch.setChecked(true);

                        if(ds.child("attendees").child(userID).child("driver").getValue(Boolean.class)) {
                            driverSwitch.setChecked(true);
                        }
                        else {
                            driverSwitch.setChecked(false);
                        }

                        // can only become a driver if they are going
                        driverLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        goingSwitch.setChecked(false);
                    }

                }
                Log.d("AHHH", " "+eventDescription);
                eventPageDescription = findViewById(R.id.eventPageDescription);
                eventPageDescription.setText(eventDescription);
                SpannableString spanStr;
                if (numAttendees == 1) {
                    spanStr = new SpannableString("1 person going");
                }
                else {
                    spanStr = new SpannableString(String.valueOf(numAttendees) + " people going!");
                }
                spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
                goingMembers.setText(spanStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // perform query
        eventDescQuery.addListenerForSingleValueEvent(valueEventListener);

        // load all the event info and display it
        eventPageGroupTitle = findViewById(R.id.eventPageGroupTitle);
        eventPageEventTitle = findViewById(R.id.eventPageEventTitle);

        eventPageGroupTitle.setText(groupName);
        eventPageEventTitle.setText(eventName);

        // handle when the user switches their status to going

        goingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // add the person to attendees
                    dbGroup.child("members").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            User attendee = new User(false, false, userID, user.getUserName(), user.getPhoneNumber(), user.getAddress());
                            Map<String, Object> updateAttendee = new HashMap<>();
                            updateAttendee.put(userID, attendee);
                            eventsRef.child(eventName).child("attendees").updateChildren(updateAttendee);

                            Map<String, Object> updateEvents = new HashMap<>();
                            updateEvents.put(eventName, true);
                            dbUsers.child(userID).child("groups").child(groupName).child("events").updateChildren(updateEvents);

                            // add one to the number of attendees and update the text

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    // delete his invitation
                    dbUsers.child(userID).child("eventInvites").child(groupCode).child("events").child(eventName).removeValue();
                }
                else {
                    // delete the person from attendees
                    eventsRef.child(eventName).child("attendees").child(userID).getRef().removeValue();
                }
            }
        });

        // handle when the user says they can drive
        driverSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // set driver to true
                   eventsRef.child(eventName).child("attendees").child(userID).child("driver").setValue(true);
                }
                else {
                    eventsRef.child(eventName).child("attendees").child(userID).child("driver").setValue(false);
                }
            }
        });

    }

    public void onViewGoingMembers(View view) {
        Query query = eventsRef
                .orderByChild("eventName")
                .equalTo(eventName);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EventPage.this);
                builder.setTitle("Attendees");
                ArrayList<String> userList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.child(eventName).child("attendees").getChildren()) {
                    User user = ds.getValue(User.class);
                    userList.add(user.getUserName());
                }
                CharSequence[] cs = userList.toArray(new CharSequence[userList.size()]);
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    public void onInviteFriends(View view) {
        selectedMembersIndexList = new ArrayList<>();
        members.clear();
        dbGroups = db.getReference("groups");
        dbGroup = dbGroups.child(groupCode);
        dbMembers = dbGroup.child("members");

        // query and grab the map of members to display later
        Query query = dbGroups
                .orderByChild("groupCode")
                .equalTo(groupCode);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("members").getValue(genericTypeIndicator) != null) {
                        membersMap = ds.child("members").getValue(genericTypeIndicator);
                    }
                }

                // grab the name of each member from the membersMap except themself
                for (String memberID : membersMap.keySet()) {
                    User user = membersMap.get(memberID);

                    if (!user.getUserID().equals(userID)) {
                        members.add(user.getUserName());
                        memberIDs.add(user.getUserID());
                    }
                }

                memberSequence = members.toArray(new CharSequence[members.size()]);

                // display the members
                final boolean[] isSelectedArray = new boolean[members.size()];

                // build the dialog popup for inviting people
                AlertDialog.Builder builder = new AlertDialog.Builder(EventPage.this);

                builder.setTitle("Choose who you want to invite!")
                        .setMultiChoiceItems(memberSequence, isSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    if (! selectedMembersIndexList.contains(which)) {
                                        selectedMembersIndexList.add(which);
                                    }
                                }
                                else if (selectedMembersIndexList.contains(which)) {
                                    selectedMembersIndexList.remove(which);
                                }
                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked invite

                                for (int i = 0; i < selectedMembersIndexList.size(); i++) {
                                    // for each selected user, query the user and
                                    // add this event to their event invites
                                    Query query = dbUsers
                                            .orderByChild("userID")
                                            .equalTo(memberIDs.get(selectedMembersIndexList.get(i)));

                                   // final Map<String, Object> groupName = new HashMap<>();
                                   // groupName.put("groupName", groupName);

                                    final Map<String, Object> events = new HashMap<>();
                                    events.put(eventName, true);

                                    final Map<String, Object> invited = new HashMap<>();

                                    ValueEventListener valueEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                groupRef = ds.getRef().child("eventInvites").child(groupCode);
                                                groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                                                        // add the event to the user's invited events
                                                        dataSnapshots.getRef().child("events").updateChildren(events);

                                                        // if it doesnt already have the groupName, add the groupName
                                                        if (!dataSnapshots.child("groupName").exists()) {
                                                            dataSnapshots.getRef().child("groupName").setValue(groupName);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                                userName = ds.child("userName").getValue().toString();
                                                invitedUserID = ds.child("userID").getValue().toString();
                                                // code to send notification to the user? need FCM
                                            }

                                            // add them to the invited list
                                            invited.put(invitedUserID, userName);
                                            dbGroup.child("events").child(eventName).child("invited").updateChildren(invited);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    };

                                    query.addListenerForSingleValueEvent(valueEventListener);
                                    Log.d("Invited", memberIDs.get(selectedMembersIndexList.get(i)));
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user clicked cancel
                                Log.d("Invited", "CANCELED!");
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < isSelectedArray.length; i++) {
                                    isSelectedArray[i] = false;
                                }
                                selectedMembersIndexList.clear();
                            }
                        });

                // create and show the dialog
                AlertDialog mDialog = builder.create();
                mDialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    public void onGenerateCars(View view) {
        Intent intent = new Intent(this, GenerateCars.class);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupCode", groupCode);
        intent.putExtra("eventName", eventName);
        startActivity(intent);
    }
}
