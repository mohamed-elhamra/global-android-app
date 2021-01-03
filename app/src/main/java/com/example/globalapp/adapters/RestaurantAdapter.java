package com.example.globalapp.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.globalapp.MainActivity;
import com.example.globalapp.R;
import com.example.globalapp.model.Restaurant;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;


public class RestaurantAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private ArrayList<Restaurant> restaurants;
    CustomFilter filter;
    ArrayList<Restaurant> filterList;

    LocationManager locationManager;

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
        this.filterList = restaurants;
    }

    @Override
    public int getCount() {
        return this.restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return this.restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.restaurants.indexOf(getItem(position));
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.restaurant_cell, parent, false);

        TextView restaurantName = convertView.findViewById(R.id.restaurantName);
        TextView restaurantPhone = convertView.findViewById(R.id.restaurantPhone);
        TextView restaurantDistance = convertView.findViewById(R.id.distance);

        restaurantName.setText(restaurantName.getText().toString().concat(restaurants.get(position).getName()));
        restaurantPhone.setText(restaurantPhone.getText().toString().concat(" ").concat(restaurants.get(position).getAddress()));

        getCurrentLocation(restaurantDistance, restaurants.get(position));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            this.filter = new CustomFilter();
        }
        return filter;
    }

    private void getCurrentLocation(TextView restaurantDistance, Restaurant restaurant) {
        LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Location destination = new Location("destination");
                destination.setLatitude(restaurant.getLat());
                destination.setLongitude(restaurant.getLon());
                restaurantDistance.setText(restaurantDistance.getText()
                        .toString().concat(" ").concat(String.valueOf(location.distanceTo(destination) / 1000).concat(" ").concat("km")));
                if (restaurantDistance.getText().toString().length() > 0)
                    locationManager.removeUpdates(this);
            }
        };
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, mLocationListener);
    }


    class CustomFilter extends Filter {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                final CharSequence constraintUpperCase = constraint.toString().trim().toUpperCase();
                ArrayList<Restaurant> filteredRestaurantsList = new ArrayList<>();

                RestaurantAdapter.this.filterList.forEach(restaurant -> {
                    if (restaurant.getName().toUpperCase().contains(constraintUpperCase)) {
                        filteredRestaurantsList.add(restaurant);
                    }
                });

                filterResults.count = filteredRestaurantsList.size();
                filterResults.values = filteredRestaurantsList;
            } else {
                filterResults.count = RestaurantAdapter.this.filterList.size();
                filterResults.values = RestaurantAdapter.this.filterList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            RestaurantAdapter.this.restaurants = (ArrayList<Restaurant>) results.values;
            notifyDataSetChanged();
        }
    }
}
