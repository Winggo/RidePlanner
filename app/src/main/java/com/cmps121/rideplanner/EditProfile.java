package com.cmps121.rideplanner;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbUsers;
    FirebaseUser user;

    EditText nameField;
    EditText phoneNumberField;
    EditText addressField;

    String userID;
    String name;
    String phoneNumber;
    String address;

    Button editName;
    Button editPhone;
    Button editAddress;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // initialize info
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        dbUsers = db.getReference("users");

        // initialize editTexts
        nameField = findViewById(R.id.nameInput);
        phoneNumberField = findViewById(R.id.phoneInput);
        addressField = findViewById(R.id.addressInput);

        // set all editables as false for now
        nameField.setEnabled(false);
        phoneNumberField.setEnabled(false);
        addressField.setEnabled(false);

        // querying the user info if it exists, and displaying it as profile info
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("userID")
                .equalTo(userID);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("NewDebug", "onDataChange");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    // if profile has already been created, load the info and set the text
                    if (ds.child("profileCreated").getValue(Boolean.class)) {
                        nameField.setText(ds.child("userName").getValue().toString());
                        phoneNumberField.setText(ds.child("phoneNumber").getValue().toString());
                        addressField.setText(ds.child("address").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addListenerForSingleValueEvent(valueEventListener);

        // set onFocus listeners to make text editable/non-editable
        nameField.setOnFocusChangeListener(this);
        phoneNumberField.setOnFocusChangeListener(this);
        addressField.setOnFocusChangeListener(this);

        // Implementing onClickListeners for all the buttons
        editName = (Button) findViewById(R.id.editNameBtn);
        editName.setOnClickListener(this);

        editPhone = (Button) findViewById(R.id.editPhoneBtn);
        editPhone.setOnClickListener(this);

        editAddress = (Button) findViewById(R.id.editAddressBtn);
        editAddress.setOnClickListener(this);

        saveButton = (Button) findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(this);
    }

    public void onSave(View view) {
        // get all edited parts of the text
        name = nameField.getText().toString();
        phoneNumber = phoneNumberField.getText().toString();
        address = addressField.getText().toString();

       // dbHelper.updateInfoByUID(userID, name, phoneNumber, address, true);

        // update the db with the new info
        dbUsers = FirebaseDatabase.getInstance().getReference("users");
        final Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("userName", name);
        userUpdates.put("phoneNumber", phoneNumber);
        userUpdates.put("address", address);
        userUpdates.put("profileCreated", true);

        // query all users that have our userID (should only be one)
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("userID")
                .equalTo(userID);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().updateChildren(userUpdates);
                }
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addListenerForSingleValueEvent(valueEventListener);

        Toast.makeText(this, "Successful!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // onClick method to handle all button clicks
    @Override
    public void onClick(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (view.getId()) {
            case R.id.editNameBtn:
                nameField.setEnabled(true);
                nameField.requestFocus();
                // Show soft keyboard for the user to enter the value.
                inputMethodManager.showSoftInput(nameField, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.editPhoneBtn:
                phoneNumberField.setEnabled(true);
                phoneNumberField.requestFocus();
                inputMethodManager.showSoftInput(phoneNumberField, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.editAddressBtn:
                addressField.setEnabled(true);
                addressField.requestFocus();
                inputMethodManager.showSoftInput(addressField, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.saveBtn:
                onSave(view);
        }
    }

    // onFocusChange method for all the focus changes

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch(view.getId()) {
            case R.id.nameInput:
                if (!hasFocus) {
                    // hide the soft keyboard
                    inputMethodManager.hideSoftInputFromWindow(nameField.getWindowToken(), 0);
                    // make it non-editable
                    nameField.setEnabled(false);
                }
                break;

            case R.id.phoneInput:
                if (!hasFocus) {
                    inputMethodManager.hideSoftInputFromWindow(phoneNumberField.getWindowToken(), 0);
                    // make it non-editable
                    phoneNumberField.setEnabled(false);
                }
                break;

            case R.id.addressInput:
                if (!hasFocus) {
                    inputMethodManager.hideSoftInputFromWindow(addressField.getWindowToken(), 0);
                    // make it non-editable
                    addressField.setEnabled(false);
                }
                break;
        }
    }
}
