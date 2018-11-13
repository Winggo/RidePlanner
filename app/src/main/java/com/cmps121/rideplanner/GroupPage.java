package com.cmps121.rideplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GroupPage extends AppCompatActivity {

    String groupName;
    TextView groupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        // get reference of title textview
        groupTitle = (findViewById(R.id.groupName));

        // Grab group name from intent extras
        groupName = getIntent().getStringExtra("groupClicked");

        // Display the group name
        groupTitle.setText(groupName);
    }
}
