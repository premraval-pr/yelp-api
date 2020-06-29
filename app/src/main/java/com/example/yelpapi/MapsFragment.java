package com.example.yelpapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment {

    double lat,lon;
    SupportMapFragment mapFragment;

    YelpAdapter yelpAdapter;
    public MapsFragment(double lat, double lon, YelpAdapter yelpAdapter){
        this.lat = lat;
        this.lon = lon;
        this.yelpAdapter = yelpAdapter;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            LatLng currentLocation = new LatLng(lat,lon);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,12));

            googleMap.clear();

            List<Place> placeList = yelpAdapter.getmListData();

            for (Place p: placeList) {
                LatLng tempLat = new LatLng(p.getLatitude(),p.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(tempLat).title(p.getName()).snippet(p.getId()));
                Log.d("MAPS", "onMapReady: " + p.getName() + p.getLatitude() +" - " +p.getLongitude());
            }

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onMarkerClick(Marker marker) {
                    startActivity(new Intent(
                            getContext(),
                            DetailActivity.class).putExtra("BUSINESS", Objects.requireNonNull(yelpAdapter.getItemById(marker.getSnippet()))));
                    return false;
                }
            });
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects.requireNonNull(getContext()),R.raw.dark_map));
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void updateMarker(){
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

}