package com.realestate_userapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestate_userapp.Models.HistoryModel;
import com.realestate_userapp.Models.PropertyModel;
import com.realestate_userapp.MoreDetailsActivity;
import com.realestate_userapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import jp.wasabeef.glide.transformations.BlurTransformation;


import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {
    private List<PropertyModel> category;
    private Context context;

    public PurchaseAdapter(Context context, List<PropertyModel> category) {
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public PurchaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wrapper_purchase, parent, false);
        return new PurchaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.ViewHolder holder, int position) {
        PropertyModel destination = category.get(position);
        holder.propertyname.setText(destination.getPropertyName());
        holder.brokername.setText(destination.getArea() + ", " + destination.getCity());
        holder.rent.setText("Rs. " + destination.getRent() + "/-");

        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("Property_images");

        imagesRef.orderByChild("propertyId").equalTo(destination.getPropertyId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Get the image URL
                            String imageUrl = snapshot.child("imageurl").getValue(String.class);

                            Picasso.get().load(imageUrl).into(holder.imageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                        databaseError.toException().printStackTrace();
                    }
                });

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("propname", destination.getPropertyName());
                bundle.putString("rent", destination.getRent());
                bundle.putString("rooms", destination.getRooms());
                bundle.putString("parking", destination.getParking());
                bundle.putString("nearbyplace", destination.getNearbyPlace());
                bundle.putString("floors", destination.getFloors());
                bundle.putString("area", destination.getArea());
                bundle.putString("city", destination.getCity());
                bundle.putString("propertyid", destination.getPropertyId());
                bundle.putString("gym", destination.getGym());
                bundle.putString("brokerid", destination.getBrokerid());
                // Start the activity and pass the Bundle
                Log.e("naaaam", destination.getNearbyPlace());
                Intent intent = new Intent(context, MoreDetailsActivity.class);
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
        TextView propertyname, brokername, rent;
        LinearLayout linear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.leftIcon);
            propertyname = itemView.findViewById(R.id.name);
            brokername = itemView.findViewById(R.id.brokername);
            rent = itemView.findViewById(R.id.price);
            linear = itemView.findViewById(R.id.linear);
        }
    }
}
