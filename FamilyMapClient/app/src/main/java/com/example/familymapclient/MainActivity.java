package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import Model.DataModel;
import Model.Singleton;

public class MainActivity extends AppCompatActivity implements LoginListener {

    private LoginFragment loginFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataModel dataModel = Singleton.getInstance();

        if (dataModel.getAuthToken() == null) {
            mapFragment = null;
            FragmentManager fm = this.getSupportFragmentManager();
            loginFragment = LoginFragment.newInstance();
            fm.beginTransaction().add(R.id.frag, loginFragment).commit();
        }
        else {
            onLogin();
        }
    }

    public void onLogin() {
        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = new MapFragment();
        fm.beginTransaction().add(R.id.frag, mapFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_fragment, menu);
        return true;    // return true to display menu
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem itemSearch = menu.findItem(R.id.search);
        MenuItem itemSettings = menu.findItem(R.id.settings);

        if(mapFragment != null && mapFragment.getEventID() == null) {
            itemSearch.setVisible(true);
            itemSettings.setVisible(true);
        }
        else {
            itemSearch.setVisible(false);
            itemSettings.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        DataModel dataModel = Singleton.getInstance();

        if (dataModel.getAuthToken() == null) {
            setContentView(R.layout.activity_main);
            mapFragment = null;
            FragmentManager fm = this.getSupportFragmentManager();
            loginFragment = LoginFragment.newInstance();
            fm.beginTransaction().add(R.id.frag, loginFragment).commit();
        }
        else if (mapFragment != null) {
            mapFragment.startMap();
        }
        super.onNewIntent(intent);

    }
}

