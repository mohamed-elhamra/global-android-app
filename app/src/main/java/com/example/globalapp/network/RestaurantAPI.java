package com.example.globalapp.network;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.globalapp.database.DataBaseHelper;
import com.example.globalapp.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RestaurantAPI extends AsyncTask<DataBaseHelper, Void, ArrayList<Restaurant>> {

    private final static String API_URL = "https://developers.zomato.com/api/v2.1/search";

    private DataBaseHelper db;

    @Override
    protected void onPostExecute(ArrayList<Restaurant> restaurants) {
        this.db.saveRestaurantsListFromAPI(restaurants);
    }

    @Override
    protected ArrayList<Restaurant> doInBackground(DataBaseHelper... dataBaseHelpers) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        this.db = dataBaseHelpers[0];
        try {
            Log.wtf("API", "Call API 1");

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(API_URL)
                    .get()
                    .addHeader("user-key", "85db520fdfd235859826f37a4f240306")
                    .build();

            Response response = client.newCall(request).execute();
            String data = response.body().string();
            Log.wtf("API", data);
            JSONObject jsonObject = new JSONObject(data);

            JSONArray jsonArray = jsonObject.getJSONArray("restaurants");
            long lengthJsonArray = jsonArray.length();

            for (int i = 0; i < lengthJsonArray; i++) {
                JSONObject currentObject = jsonArray.getJSONObject(i).getJSONObject("restaurant");
                Restaurant restaurant = Restaurant.builder()
                        .name(currentObject.getString("name"))
                        .phone(currentObject.getString("phone_numbers").split(",")[0].trim())
                        .website(currentObject.getString("url"))
                        .address(currentObject.getJSONObject("location").getString("address")
                                + ", " + currentObject.getJSONObject("location").getString("city"))
                        .menu(currentObject.getString("menu_url"))
                        .lat(currentObject.getJSONObject("location").getDouble("latitude"))
                        .lon(currentObject.getJSONObject("location").getDouble("longitude"))
                        .build();

                if (i % 2 == 0) {
                    restaurant.setCategory("Fast food");
                } else if (i % 3 == 0) {
                    restaurant.setCategory("Casual dining");
                } else if (i % 5 == 0) {
                    restaurant.setCategory("Family style");
                } else {
                    restaurant.setCategory("Other");
                }
                restaurants.add(restaurant);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurants;
    }
}
