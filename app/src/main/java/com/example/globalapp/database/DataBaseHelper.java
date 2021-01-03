package com.example.globalapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.globalapp.model.Restaurant;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper INSTANCE;

    private DataBaseHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    public static synchronized DataBaseHelper getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new DataBaseHelper(context.getApplicationContext());

        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(email text primary key, password text)");
        db.execSQL("create table restaurant(name text primary key, phone text, website text, address text, menu text, lat REAL, lon REAL, category text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insert(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long ins = db.insert("user", null, contentValues);
        return ins != -1;
    }

    public boolean isRestaurantTableEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT count(*) FROM restaurant";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count <= 0;
    }

    public void saveRestaurantsListFromAPI(ArrayList<Restaurant> restaurants){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        restaurants.forEach(restaurant -> {
            contentValues.put("name", restaurant.getName());
            contentValues.put("phone", restaurant.getPhone());
            contentValues.put("website", restaurant.getWebsite());
            contentValues.put("address", restaurant.getAddress());
            contentValues.put("menu", restaurant.getMenu());
            contentValues.put("lat", restaurant.getLat());
            contentValues.put("lon", restaurant.getLon());
            contentValues.put("category", restaurant.getCategory());
            db.insert("restaurant", null, contentValues);
        });
    }

    public ArrayList<Restaurant> getRestaurantsListByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        String query = "SELECT * FROM restaurant WHERE category = ?";
        Cursor cursor = db.rawQuery(query, new String[]{category});
        while (cursor.moveToNext()){
            Restaurant restaurant = Restaurant.builder()
                    .name(cursor.getString(cursor.getColumnIndexOrThrow("name")))
                    .phone(cursor.getString(cursor.getColumnIndexOrThrow("phone")))
                    .website(cursor.getString(cursor.getColumnIndexOrThrow("website")))
                    .address(cursor.getString(cursor.getColumnIndexOrThrow("address")))
                    .menu(cursor.getString(cursor.getColumnIndexOrThrow("menu")))
                    .lat(cursor.getDouble(cursor.getColumnIndexOrThrow("lat")))
                    .lon(cursor.getDouble(cursor.getColumnIndexOrThrow("lon")))
                    .category(cursor.getString(cursor.getColumnIndexOrThrow("category")))
                    .build();
            restaurants.add(restaurant);
        }
        cursor.close();
        return restaurants;
    }
    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email = ?", new String[]{email});
        if (cursor.getCount() > 1) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public Boolean userExist(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email = ? and password = ?",
                new String[]{email, password});
        if(cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public ArrayList<String> getCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"category"};
        ArrayList<String> categories = new ArrayList<>();

        Cursor cursor = db.query(true,"restaurant", projection ,
                null, null, projection[0],null, null, null);
        while (cursor.moveToNext()){
            categories.add(cursor.getString(cursor.getColumnIndexOrThrow("category")));
        }
        cursor.close();
        return categories;
    }
}
