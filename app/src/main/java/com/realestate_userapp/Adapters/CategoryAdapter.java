package com.realestate_userapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realestate_userapp.CategoryDetailsActivity;
import com.realestate_userapp.Models.CategoryModel;
import com.realestate_userapp.Models.PropertyModel;
import com.realestate_userapp.MoreDetailsActivity;
import com.realestate_userapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryModel> category;
    private Context context;

    public CategoryAdapter(Context context, List<CategoryModel> category) {
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wrapper_categories, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryModel destination = category.get(position);
        Picasso.get().load(destination.getImageUrl()).into(holder.imageView);
        holder.propertyname.setText(destination.getType());

        holder.totallayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", destination.getType());
                bundle.putString("id", destination.getId());
                // Start the activity and pass the Bundle
                Log.e("naaaam", destination.getType());
                Intent intent = new Intent(context, CategoryDetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView propertyname;
        RelativeLayout totallayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            propertyname = itemView.findViewById(R.id.name);
            totallayout = itemView.findViewById(R.id.Totallayout);
        }
    }
}
