package com.track.trackandtrigger;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
                .load(R.drawable.ic__category)
                .into(holder.category_image);
        holder.category_title.setText(categoryTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView category_image;
        TextView category_title;
        CardView category_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_image =itemView.findViewById(R.id.category_image);
            category_title = itemView.findViewById(R.id.category_title);
            category_card =itemView.findViewById(R.id.category_card);
            category_card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(v.getContext(),CategoryActivity.class);
            intent.putExtra("Category",categoryTitles.get(position));
            v.getContext().startActivity(intent);
        }
    }
}
