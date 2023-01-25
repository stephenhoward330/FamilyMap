package com.example.familymapclient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class EventActivity extends AppCompatActivity {

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putString("eventID", getIntent().getStringExtra("eventID"));
        mapFragment.setArguments(bundle);

        fm.beginTransaction().add(R.id.frag, mapFragment).commit();
    }
}
