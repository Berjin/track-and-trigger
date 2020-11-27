package com.track.trackandtrigger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryRecyclerviewAdapter extends RecyclerView.Adapter<CategoryRecyclerviewAdapter.ViewHolder> {
    private ArrayList<String> categoryTitles;
    private Context context;

    public CategoryRecyclerviewAdapter(Context context, ArrayList<String> categoryTitles) {
        this.categoryTitles = categoryTitles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .asBitmap()
                .load(R.drawable.ic_grocery)
                .into(holder.category_image);
        holder.category_title.setText(categoryTitles.get(position));
        holder.category_image.setOnClickListener(v -> {
            Toast.makeText(context, categoryTitles.get(position)+" "+position, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return categoryTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView category_image;
        TextView category_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_image =itemView.findViewById(R.id.category_image);
            category_title = itemView.findViewById(R.id.category_title);
        }
    }
}
