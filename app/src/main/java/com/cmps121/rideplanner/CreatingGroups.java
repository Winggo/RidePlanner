package com.cmps121.rideplanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    Button copy;

    String groupName;
    String userID;
    String userName;
    String userAddress;
    String userPhoneNumber;
    String groupID = "";
    // ACTION BAR
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_groups);

        // ACTION BAR
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(bottomListener);

        db = FirebaseDatabase.getInstance();
        dbGroups = db.getReference("groups");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        groupInput = (EditText) findViewById(R.id.groupField);
        createButton = (Button) findViewById(R.id.createBtn);
        shareTitle = (TextView) findViewById(R.id.shareTitle);
        shareText = (TextView) findViewById(R.id.shareText);
        groupCode = (TextView) findViewById(R.id.codeHeader);
        groupCode = (TextView) findViewById(R.id.groupCode);
        copy = (Button) findViewById(R.id.copy);

        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCreateBtn(view);
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
//                        case R.id.logout:
//                            signOut(getWindow().getDecorView().getRootView());
//                            return true;
                    }
                    return false;
                }
            };

    public void onCreateBtn(View view) {
        groupName = groupInput.getText().toString();
        groupID = dbGroups.push().getKey();

        // if the user entered an invalid group name, don't let them create it
        if (groupName == null || !groupName.matches("^[a-zA-Z0-9 ]+$") || groupName.isEmpty()) {
            finish();
            startActivity(getIntent());
            Toast.makeText(getApplicationContext(), "Please only use letters and numbers in your group name!", Toast.LENGTH_LONG).show();

        } else {
            // otherwise let them create it
            groupCode.setText(groupID);

            Query query = FirebaseDatabase.getInstance().getReference("users")
                    .orderByChild("userID")
                    .equalTo(userID);

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> groups= new HashMap<>();
                    Map<String, Object> events = new HashMap<>();

                    // create a temporary event to initialize the user's event field
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        events.put("init", false);
                        groups.put(groupName, true);
                        ds.child("groups").getRef().updateChildren(groups);
                        ds.getRef().child("groups").child(groupName).child("events").setValue(events);

                        // grab all the info from the user while we are querying the user
                        userName = ds.child("userName").getValue().toString();
                        userAddress = ds.child("address").getValue().toString();
                        userPhoneNumber = ds.child("phoneNumber").getValue().toString();

                    }

                    // update the group with the new user
                    Map<String, User> members = new HashMap<>();
                    User user = new User(userID, userName, userPhoneNumber, userAddress, true);
                    members.put(userID, user);

                    Group group = new Group(groupName, groupID);
                    dbGroups.child(groupID).setValue(group);
                    dbGroups.child(groupID).child("members").setValue(members);
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
            copy.setVisibility(View.VISIBLE);

        }
    }

    public void copyText(View view) {
        ClipboardManager cb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("groupID", groupID);
        cb.setPrimaryClip(clip);

        Toast.makeText(this,"Code copied onto clipboard", Toast.LENGTH_SHORT).show();
    }

}
