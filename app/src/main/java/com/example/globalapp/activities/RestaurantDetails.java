package com.example.globalapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.globalapp.R;
import com.example.globalapp.fragments.MapFragment;
import com.example.globalapp.model.Restaurant;


public class RestaurantDetails extends AppCompatActivity {

    Button btnCallRestaurant;
    private Restaurant restaurant;
    Button btnScanQRCode;
    Button btnWebsite;
    Button btnMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_details);

        bindWidgets();

        Fragment fragment = new MapFragment(restaurant);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();

        callRestaurantListener();
        scanQRCodeListener();
        websiteListener();
        menuListener();
    }

    private void scanQRCodeListener() {
        btnScanQRCode.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyCodeScanner.class);
            startActivity(intent);
        });
    }

    private void bindWidgets() {
        restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
        btnCallRestaurant = findViewById(R.id.btnCallRestaurant);
        btnScanQRCode = findViewById(R.id.btnScanner);
        btnWebsite = findViewById(R.id.btnWebsite);
        btnMenu = findViewById(R.id.btnMenu);
    }

    private void menuListener() {
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Log.wtf("MENU", "********************************" + restaurant.getMenu());
            Uri uri = Uri.parse(restaurant.getMenu());
            intent.setData(uri);
            startActivity(intent);
        });
    }

    private void websiteListener() {
        btnWebsite.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(restaurant.getWebsite());
            intent.setData(uri);
            startActivity(intent);
        });
    }

    private void callRestaurantListener() {
        btnCallRestaurant.setOnClickListener(v -> {
            Uri uri = Uri.parse("tel:" + restaurant.getPhone());
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
        });
    }


}
