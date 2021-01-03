package com.example.globalapp.activities;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.globalapp.R;
import com.example.globalapp.adapters.RestaurantAdapter;
import com.example.globalapp.database.DataBaseHelper;
import com.example.globalapp.model.Restaurant;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RestaurantList extends AppCompatActivity {

    ListView restaurantsList;
    RestaurantAdapter adapter;
    SearchView searchView;
    DataBaseHelper db;
    ArrayList<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        bindWidgets();

        db = DataBaseHelper.getInstance(this);
        fedRestaurants();

        adapter = new RestaurantAdapter(this, restaurants);
        restaurantsList.setAdapter(adapter);

        searchViewListener();

        restaurantsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RestaurantList.this, RestaurantDetails.class);
                intent.putExtra("restaurant", restaurants.get(position));
                startActivity(intent);
            }
        });
    }

    private void searchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RestaurantList.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RestaurantList.this.adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void bindWidgets(){
        searchView = findViewById(R.id.searchView);
        restaurantsList = findViewById(R.id.restaurantsList);
    }

    private void fedRestaurants() {
        String category = getIntent().getStringExtra("category");
        restaurants = db.getRestaurantsListByCategory(category);
    }

}