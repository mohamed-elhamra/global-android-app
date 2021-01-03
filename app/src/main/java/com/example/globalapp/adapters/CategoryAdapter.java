package com.example.globalapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globalapp.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private final ArrayList<String> categories;
    private final LayoutInflater layoutInflater;
    private final ItemClickListener itemClickListener;

    public CategoryAdapter(Context context, ArrayList<String> categories, ItemClickListener itemClickListener){
        this.layoutInflater = LayoutInflater.from(context);
        this.categories = categories;
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final Button categoryButton;
        public ViewHolder(View itemView){
            super(itemView);
            this.categoryButton = itemView.findViewById(R.id.categoryButton);
            categoryButton.setOnClickListener(v -> itemClickListener.onItemClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.category_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.categoryButton.setText(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

}
