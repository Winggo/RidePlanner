package com.cmps121.rideplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmps121.rideplanner.ui.yourcar.YourCarFragment;

public class yourCar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_car_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, YourCarFragment.newInstance())
                    .commitNow();
        }
    }
}
