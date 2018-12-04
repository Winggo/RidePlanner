package com.cmps121.rideplanner;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
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

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbUsers;
    String userID;
    FirebaseUser user;
    Button viewInvitesBtn;
    Integer inviteCount;
    // ACTION BAR
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ACTION BAR
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(bottomListener);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {

            inviteCount = 0;

            // user has already signed in
            user = FirebaseAuth.getInstance().getCurrentUser();
            userID = user.getUid();
            dbUsers = db.getReference("users");

            // query to find the amount of invites the user has

            dbUsers.child(userID).child("eventInvites").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("Events", "in ondatachange");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("Events", "adding " + String.valueOf(ds.getChildrenCount()));
                        inviteCount += (int) ds.child("events").getChildrenCount();
                    }
                    //viewInvitesBtn = findViewById(R.id.viewInvitesBtn);
                    //viewInvitesBtn.setText(String.valueOf(inviteCount));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            createSignInIntent();
        }

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

    // route to creating group page. might be able to just set content view. not sure

    public void onCreateGroup(View view) {
        Intent intent = new Intent(this, CreatingGroups.class);
        startActivity(intent);
    }

    public void onJoinGroup(View view) {
        try{
        Intent intent = new Intent(this, JoiningGroups.class);
        startActivity(intent);}
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "Please set up your user profile!", Toast.LENGTH_SHORT).show();

        }
    }

    public void onViewGroups(View view) {
        try{
        Intent intent = new Intent(this, ViewGroups.class);
        startActivity(intent);}
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "Please set up your user profile!", Toast.LENGTH_SHORT).show();

        }
    }

    public void onEditProfile(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    public void onViewInvites(View view) {
        Intent intent = new Intent (this, ViewInvites.class);
        startActivity(intent);
    }

    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.PersonalTheme)
                        .setLogo(R.mipmap.rpe)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                userID = user.getUid();
                dbUsers = db.getReference("users");
                dbUsers.orderByChild("userID").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            // we already have the userID. check if the profile has been created or not
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (!data.child("profileCreated").getValue(Boolean.class)) {
                                    Toast.makeText(getApplicationContext(), "Please set up your user profile!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else {
                            // add the userID to the user database

                            HashMap<String, Boolean> tempMap = new HashMap<>();

                            User dbUser = new User(user.getUid(), user.getDisplayName(), false, tempMap);
                            dbUsers.child(user.getUid()).setValue(dbUser);
                            Toast.makeText(getApplicationContext(), "Please set up your user profile!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                if (resultCode == RESULT_CANCELED) {
                    // do nothing
                }
                else {
                    Toast.makeText(getApplicationContext(), response.getError().getMessage(), Toast.LENGTH_SHORT);
                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    public void signOut(View view) {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        createSignInIntent();
                    }
                });
        // [END auth_fui_signout]
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
