package com.example.globalapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;

import com.example.globalapp.activities.Login;
import com.example.globalapp.activities.Registration;
import com.example.globalapp.database.DataBaseHelper;
import com.example.globalapp.network.RestaurantAPI;
import com.example.globalapp.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity {

    Button signIn, registration;
    DataBaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindWidget();
        signInOnClickListener();
        registrationOnClickListener();
    }

    private void bindWidget() {
        db = DataBaseHelper.getInstance(this);
        if (db.isRestaurantTableEmpty()) {
            new RestaurantAPI().execute(db);
        }
        signIn = findViewById(R.id.sign_in);
        registration = findViewById(R.id.sign_up);
    }

    private void signInOnClickListener() {
        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });
    }

    private void registrationOnClickListener() {
        registration.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registration.class);
            startActivity(intent);
        });
    }

}