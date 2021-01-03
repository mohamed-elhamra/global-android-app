package com.example.globalapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.globalapp.R;
import com.example.globalapp.model.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment {

    private final Restaurant restaurant;

    public MapFragment(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //when marker is loaded
                LatLng latLng = new LatLng(restaurant.getLat(), restaurant.getLon());

                //when clicked on map initialize marker options
                MarkerOptions markerOptions = new MarkerOptions();
                //set position of marker
                markerOptions.position(latLng);
                //set title of marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                //remove all marker
                googleMap.clear();
                //animating to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                //add marker on map
                googleMap.addMarker(markerOptions);
            }
        });
        return view;
    }
}