package com.cmps121.rideplanner;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
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
import com.google.firebase.iid.FirebaseInstanceId;

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

    static String holdTheamount;

    static boolean areDriver = false;
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

    String driverID;

    GenericTypeIndicator<Map<String, User>> genericTypeIndicator;

    List<String> members = new ArrayList<>();
    List<String> memberIDs = new ArrayList<>();
    Map<String, User> membersMap = new HashMap<>();
    CharSequence[] memberSequence;

    User newUser;
    //need to fix
    Event event;
    Downloader dwn = new Downloader();
    Button generateRides;
    String eventLocation;

    // ACTION BAR
    private ActionBar toolbar;


    private ArrayList<Integer> selectedMembersIndexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        // ACTION BAR
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(bottomListener);

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
        generateRides = findViewById(R.id.generateCarsBtn);


        // set database reference to the current group
        groupsRef = db.getReference("groups");
        dbGroup = groupsRef.child(groupCode);
        eventsRef = dbGroup.child("events");
        dbUsers = db.getReference("users");


        // query to get the user's driver id
        eventsRef.child(eventName).child("attendees")
                .orderByChild("userID")
                .equalTo(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            driverID = ds.child("inCar").getValue().toString();
                            if (ds.child("eventModerator").getValue(Boolean.class)) {
                                generateRides.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
                    event = ds.getValue(Event.class);
                    eventLocation = event.getEventLocation();
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
                    // can only become a driver if they are going
                    driverLayout.setVisibility(View.VISIBLE);
                    dbGroup.child("members").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            newUser = dataSnapshot.getValue(User.class);
                           // User attendee = new User(false, false, false, userID, newUser.getUserName(), newUser.getPhoneNumber(), newUser.getAddress());
                           // Map<String, Object> updateAttendee = new HashMap<>();
                           // updateAttendee.put(userID, attendee);
                            eventsRef.child(eventName).child("attendees")
                                    .orderByChild("userID")
                                    .equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User tempUser;
                                    if (dataSnapshot.hasChild(userID)) {
                                        tempUser = dataSnapshot.child(userID).getValue(User.class);
                                    }
                                    else {
                                        tempUser = new User(false, false, "false", userID, newUser.getUserName(), newUser.getPhoneNumber(), newUser.getAddress());
                                    }
                                    Map<String, Object> updateAttendee = new HashMap<>();
                                    updateAttendee.put(userID, tempUser);
                                    eventsRef.child(eventName).child("attendees").updateChildren(updateAttendee);

                                    Map<String, Object> updateEvents = new HashMap<>();
                                    updateEvents.put(eventName, true);
                                    dbUsers.child(userID).child("groups").child(groupName).child("events").updateChildren(updateEvents);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                          /*  eventsRef.child(eventName).child("attendees").updateChildren(updateAttendee);

                            Map<String, Object> updateEvents = new HashMap<>();
                            updateEvents.put(eventName, true);
                            dbUsers.child(userID).child("groups").child(groupName).child("events").updateChildren(updateEvents);*/

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
                    driverLayout.setVisibility(View.INVISIBLE);
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


    public void onCreateDialog(View view) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(EventPage.this);
            builderSingle.setIcon(R.drawable.fui_ic_phone_white_24dp);
            builderSingle.setTitle("Select One Name:-");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EventPage.this, android.R.layout.select_dialog_singlechoice);
            arrayAdapter.add("Hardik");
            arrayAdapter.add("Archit");
            arrayAdapter.add("Jignesh");
            arrayAdapter.add("Umang");
            arrayAdapter.add("Gatti");

            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(EventPage.this);
                    builderInner.setMessage(strName);
                    builderInner.setTitle("Your Selected Item is");
                    builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                        }
                    });
                    builderInner.show();
                }
            });
            builderSingle.show();
        }







    static String justHoldmyAddress;

    public void onCreateDriverList (View view) {
            Query query = eventsRef.child(eventName).child("cars").orderByChild("driverID").equalTo(driverID);

            final  ArrayList<String> userList = new ArrayList<String>();
            final  ArrayList<String> addressButtonHolder = new ArrayList<>();

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("got7", "in ondatachange: " +dataSnapshot.getKey());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventPage.this);
                    builder.setTitle("Your Car List");
                    int i = 0;

                    //brute force way to get children within children
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // if driver is true
                        Log.d("got7", "dskey: " + ds.getKey());
                        for (DataSnapshot childrenSnapshot : ds.getChildren()) {

                            if (childrenSnapshot.hasChildren()) {
                                Log.d("got7", "in datasnapshot loop: dsKey: " + childrenSnapshot.getKey());
                                Log.d("got7", "userID: " + childrenSnapshot.getValue(User.class).getUserName());
                                User user = childrenSnapshot.getValue(User.class);
                             //   Event event = childrenSnapshot.getValue(Event.class);

                                if (user.getUserID().equals(ds.child("driverID").getValue().toString())) {
                                    Log.d("agh", "... " +event.getEventLocation());
                                //    justHoldmyAddress = eventLocation;
                                    userList.add(0, user.getUserName() + " (Driver)");
                                    addressButtonHolder.add(user.getAddress());
                                } else {
                                //    justHoldmyAddress = eventLocation;
                                    dwn = new Downloader();
                                    dwn.execute(eventLocation, user.getAddress());
                                    //going to leaave out distances from event and time it would take feature out until further notice.
//                                    userList.add(user.getUserName()+ holdTheamount);
                                    userList.add(user.getUserName()+ "\nAddress: " +user.getAddress());
                                    addressButtonHolder.add(user.getAddress());
                            }
                            }
                        }
                    }
    //sample data
//                    userList.add("Total Estimated Time of Trip - OVER 9000HOURS");
//                    userList.add("Bob Ross 2.1 Miles Away");
//                    dwn.execute("1156 High St, Santa Cruz, CA", "709 Pacific Ave, Santa Cruz, CA");
//                    userList.add("James Comey "+  holdTheamount);
                    CharSequence[] cs = userList.toArray(new CharSequence[userList.size()]);
                    builder.setItems(cs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + addressButtonHolder.get(which)));
                                startActivity(browserIntent);
                                dialog.dismiss();
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(EventPage.this, "No application can handle this request." + " Please install a webbrowser or Google Maps",  Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        }
                    });
                    builder.setPositiveButton("Route to UCSC", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=1156 High St, Santa Cruz, CA 95064"));
                                startActivity(browserIntent);
                                dialog.dismiss();
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(EventPage.this, "No application can handle this request." + " Please install a webbrowser or Google Maps",  Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Route to event", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query="+ eventLocation));
                                startActivity(browserIntent);
                                dialog.dismiss();
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(EventPage.this, "No application can handle this request." + " Please install a webbrowser or Google Maps",  Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
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





    public void onGenerateCars(View view) {
        Intent intent = new Intent(this, GenerateCars.class);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupCode", groupCode);
        intent.putExtra("eventName", eventName);
        startActivity(intent);
    }
}
