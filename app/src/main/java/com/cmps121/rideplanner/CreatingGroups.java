package com.cmps121.rideplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CreatingGroups extends AppCompatActivity {

//    private DatabaseReference db;

    private String groupName;
    private String groupEmails[];
    private String emails;
    private String delimiter = "\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_groups);

        findViewById(R.id.editText3).requestFocus();
    }

    public void onCreateGroup(View view) {
        EditText edit = (EditText)findViewById(R.id.editText3);
        groupName = edit.getText().toString();

        EditText edit2 = (EditText)findViewById(R.id.editText);
        emails = edit2.getText().toString();

        groupEmails = emails.split(delimiter);

        System.out.println(groupName);
        for(String email:groupEmails)
            System.out.println(email);

//        db = FirebaseDatabase.getInstance().getReference();
//        db.child("groups").setValue(groupName);
    }
}
