package com.example.yelpapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MapsInterface{

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 500;
    YelpAPI yelpAPI;
    AutoCompleteTextView autoCompleteTextView;
    YelpAdapter yelpAdapter;
    Handler handler;
    double lat, lon;
    MapsFragment mapsFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static final int LOCATION_CODE = 99;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            getMap();
                        }
                    }
                });

        yelpAdapter = new YelpAdapter(this, android.R.layout.simple_dropdown_item_1line);
        yelpAPI = new YelpAPI(getApplicationContext(),yelpAdapter,this);
        autoCompleteTextView = findViewById(R.id.actv_search);
        autoCompleteTextView.setAdapter(yelpAdapter);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(
                        getApplicationContext(),
                        DetailActivity.class).putExtra("BUSINESS", Objects.requireNonNull(yelpAdapter.getItem(position))));
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        yelpAPI.getSuggestedPlaces(autoCompleteTextView.getText().toString(),lat,lon);
                    }
                }
                return false;
            }
        });
    }

    private void getMap() {
        mapsFragment = new MapsFragment(lat, lon,yelpAdapter);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host,mapsFragment)
                .commit();
    }

    @Override
    public void updateMap() {
        mapsFragment.updateMarker();
    }
}