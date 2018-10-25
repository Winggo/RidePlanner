package com.cmps121.rideplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class YourGroups extends AppCompatActivity {
    Spinner sItems;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_groups);

        // I guess we can add the firebase data here for the groups
        List<String> years =  new ArrayList<String>();
        years.add("Please select you group.");

        // appends the new element at the end of the list
        for (int i=1800; i<=1805; i++)
            years.add("Group");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) findViewById(R.id.spinner1);
        sItems.setAdapter(adapter1);

    }


}
