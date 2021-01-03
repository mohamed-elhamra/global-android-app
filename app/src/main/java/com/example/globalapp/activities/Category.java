package com.example.globalapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globalapp.R;
import com.example.globalapp.adapters.CategoryAdapter;
import com.example.globalapp.database.DataBaseHelper;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Category extends AppCompatActivity implements CategoryAdapter.ItemClickListener {

    CategoryAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> categories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);

        bindWidgets();
    }

    private void bindWidgets() {
        categories = DataBaseHelper.getInstance(this).getCategories();
        recyclerView = findViewById(R.id.categoryGrid);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CategoryAdapter(this, categories, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, RestaurantList.class);
        intent.putExtra("category", categories.get(position));
        startActivity(intent);
    }
}
